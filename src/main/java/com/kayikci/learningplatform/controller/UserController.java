package com.kayikci.learningplatform.controller;

import com.kayikci.learningplatform.config.JwtService;
import com.kayikci.learningplatform.domain.Exam;
import com.kayikci.learningplatform.exception.ResourceNotFoundException;
import com.kayikci.learningplatform.repository.ExamRepository;
import com.kayikci.learningplatform.repository.QuestionRepository;
import com.kayikci.learningplatform.user.User;
import com.kayikci.learningplatform.user.UserRepository;
import com.kayikci.learningplatform.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    private final ExamRepository examRepository;
    private final QuestionRepository questionRepository;

    private final UserRepository userRepository;


    private final UserService userService;



    public UserController(ExamRepository examRepository, QuestionRepository questionRepository, UserRepository userRepository, UserService userService) {
        this.examRepository = examRepository;
        this.questionRepository = questionRepository;
        this.userRepository = userRepository;
        this.userService = userService;

    }

    @GetMapping("/authenticatedUserId/{email}")
    public ResponseEntity<Long> getAuthenticatedUser(@PathVariable(value = "email") String email) {
        User authenticatedUser = userRepository.findByEmail(email).get();
        return ResponseEntity.ok(authenticatedUser.getId());
    }

    @GetMapping("/user/{userId}/exams")
    @PreAuthorize("@userService.hasId(#userId, authentication.name)")
    public ResponseEntity<Iterable<Exam>> getAllExamsByUserId(@PathVariable(value = "userId") Long userId) {
        User user = userRepository.findById(userId).get();
        Iterable<Exam> exams = examRepository.findByUserId(user.getId());
        return ResponseEntity.ok(exams);
    }

    @GetMapping("/user/{userId}/exam/{examId}")
    @PreAuthorize("@userService.hasId(#userId, authentication.name)")
    ResponseEntity<Exam> getExamByUserId(@PathVariable(value = "userId") Long userId, @PathVariable Long examId) {
        Exam exam = examRepository.findByIdAndUserId(examId, userId).get();
        return ResponseEntity.ok(exam);
    }



    @PostMapping("/user/{userId}/exams")
    @PreAuthorize("@userService.hasId(#userId, authentication.name)")
    public ResponseEntity<Exam> createExamByUserId(@PathVariable(value = "userId") Long userId,
                                           @RequestBody Exam exam) {
        return userRepository.findById(userId).map(oldUser -> {
            exam.setUser(oldUser);
            examRepository.save(exam);
            return ResponseEntity.ok(exam);
        }).orElseThrow(() -> new ResourceNotFoundException("UserId " + userId + " not found"));
    }

    @PutMapping("/user/{userId}/exams")
    @PreAuthorize("@userService.hasId(#userId, authentication.name)")
    public Exam updateExam(@PathVariable(value = "userId") Long userId, @PathVariable Long examId, @RequestBody Exam newExam) {
        return examRepository.findByIdAndUserId(examId, userId)
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

    @DeleteMapping("/user/{userId}/exam/{examId}")
    @PreAuthorize("@userService.hasId(#userId, authentication.name)")
    public ResponseEntity<?> deleteExam(@PathVariable(value = "userId") Long userId, @PathVariable Long examId) {
        questionRepository.deleteAll(questionRepository.findByExamId(examId));
        return examRepository.findByIdAndUserId(examId, userId ).map(oldExam -> {
            examRepository.delete(oldExam);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("ExamId " + examId + " not found"));
    }


}


