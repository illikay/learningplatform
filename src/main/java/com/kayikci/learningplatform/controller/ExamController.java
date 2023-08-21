package com.kayikci.learningplatform.controller;

import com.kayikci.learningplatform.config.JwtService;
import com.kayikci.learningplatform.domain.Exam;
import com.kayikci.learningplatform.exception.ResourceNotFoundException;
import com.kayikci.learningplatform.repository.ExamRepository;
import com.kayikci.learningplatform.repository.QuestionRepository;
import com.kayikci.learningplatform.user.User;
import com.kayikci.learningplatform.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class ExamController {

    @Autowired
    private final ExamRepository examRepository;
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;



    public ExamController(ExamRepository examRepository, QuestionRepository questionRepository, UserRepository userRepository, JwtService jwtService) {
        this.examRepository = examRepository;
        this.questionRepository = questionRepository;
        this.userRepository = userRepository;
        this.jwtService = jwtService;


    }





    @GetMapping("/exam")
    @PreAuthorize("@jwtService.isTokenValidForUser(#token, authentication.name)")
    public ResponseEntity<Iterable<Exam>> getAllExamsByUserId(@RequestHeader("Authorization") String token) {
        String email = jwtService.extractUsernameForController(token);
        User user = userRepository.findByEmail(email).orElseThrow(() ->
            new ResourceNotFoundException("User not found for email:" + email));

        Iterable<Exam> exams = examRepository.findByUserId(user.getId());

        return ResponseEntity.ok(exams);
    }

    @GetMapping("/exam/{examId}")
    @PreAuthorize("@jwtService.isTokenValidForUser(#token, authentication.name)")
    ResponseEntity<Exam> getExamByUserId(@RequestHeader("Authorization") String token, @PathVariable Long examId) {
        String email = jwtService.extractUsernameForController(token);
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found for email:" + email));
        Exam exam = examRepository.findByIdAndUserId(examId, user.getId()).orElseThrow(() -> new ResourceNotFoundException("Exam not found for examId: " + examId));
        return ResponseEntity.ok(exam);
    }



    @PostMapping("/exam")
    @PreAuthorize("@jwtService.isTokenValidForUser(#token, authentication.name)")
    public ResponseEntity<Exam> createExamByUserId(@RequestHeader("Authorization") String token,
                                           @RequestBody Exam exam) {
        String email = jwtService.extractUsernameForController(token);
        return userRepository.findByEmail(email).map(oldUser -> {
            exam.setUser(oldUser);
            examRepository.save(exam);
            return ResponseEntity.ok(exam);
        }).orElseThrow(() -> new ResourceNotFoundException("Username " + email + " not found"));
    }

    @PutMapping("/exam/{examId}")
    @PreAuthorize("@jwtService.isTokenValidForUser(#token, authentication.name)")
    public Exam updateExam(@RequestHeader("Authorization") String token, @PathVariable Long examId, @RequestBody Exam newExam) {
        String email = jwtService.extractUsernameForController(token);
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found for email:" + email));
        return examRepository.findByIdAndUserId(examId, user.getId())
                .map(oldExam -> {
                    oldExam.setPruefungsName(newExam.getPruefungsName());
                    oldExam.setInfo(newExam.getInfo());
                    oldExam.setBeschreibung(newExam.getBeschreibung());
                    oldExam.setErstellDatum(newExam.getErstellDatum());
                    oldExam.setAenderungsDatum(newExam.getAenderungsDatum());
                    oldExam.setAnzahlFragen(newExam.getAnzahlFragen());
                    return examRepository.save(oldExam);
                }).orElseThrow(() -> new ResourceNotFoundException("ExamId " + examId + " not found"));
    }

    @DeleteMapping("/exam/{examId}")
    @PreAuthorize("@jwtService.isTokenValidForUser(#token, authentication.name)")
    public ResponseEntity<?> deleteExam(@RequestHeader("Authorization") String token, @PathVariable Long examId) {
        String email = jwtService.extractUsernameForController(token);
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found for email:" + email));
        questionRepository.deleteAll(questionRepository.findByExamId(examId));
        return examRepository.findByIdAndUserId(examId, user.getId() ).map(oldExam -> {
            examRepository.delete(oldExam);
            return ResponseEntity.noContent().build();
        }).orElseThrow(() -> new ResourceNotFoundException("ExamId " + examId + " not found"));
    }


}


