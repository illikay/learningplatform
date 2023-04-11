package com.kayikci.learningplatform.controller;

import com.kayikci.learningplatform.config.JwtService;
import com.kayikci.learningplatform.domain.Exam;
import com.kayikci.learningplatform.exception.ResourceNotFoundException;
import com.kayikci.learningplatform.repository.ExamRepository;
import com.kayikci.learningplatform.repository.QuestionRepository;
import com.kayikci.learningplatform.user.User;
import com.kayikci.learningplatform.user.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    private final ExamRepository examRepository;
    private final QuestionRepository questionRepository;

    private final UserRepository userRepository;
    private final JwtService jwtService;



    public UserController(ExamRepository examRepository, QuestionRepository questionRepository, UserRepository userRepository, JwtService jwtService) {
        this.examRepository = examRepository;
        this.questionRepository = questionRepository;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    /*@GetMapping("/user")
    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }*/

    @GetMapping("/user/{userId}/exams")
    public ResponseEntity<Iterable<Exam>> getAllExamsByUserId(@PathVariable(value = "userId") Integer userId,
                                                              @RequestHeader("Authorization") String bearerToken) {

        String token = bearerToken.substring(7);

        User user = userRepository.findById(userId).get();
        if (!jwtService.isTokenValid(token, user)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Iterable<Exam> exams = examRepository.findByUserId(userId);
        return ResponseEntity.ok(exams);
    }



    @PostMapping("/user/{userId}/exams")
    public ResponseEntity<Exam> createExam(@PathVariable(value = "userId") Integer userId,
                           @RequestBody Exam exam, @RequestHeader("Authorization") String bearerToken) {

        String token = bearerToken.substring(7);

        User user = userRepository.findById(userId).get();
        if (!jwtService.isTokenValid(token, user)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return userRepository.findById(userId).map(oldUser -> {
            exam.setUser(oldUser);
            examRepository.save(exam);
            return ResponseEntity.ok(exam);
        }).orElseThrow(() -> new ResourceNotFoundException("UserId " + userId + " not found"));
    }


}


