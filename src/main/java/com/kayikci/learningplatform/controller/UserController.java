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

    /*@GetMapping("/user")
    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }*/

    @GetMapping("/user/{userId}/exams")
    @PreAuthorize("@userService.hasId(#userId, authentication.name)")
    public ResponseEntity<Iterable<Exam>> getAllExamsByUserId(@PathVariable(value = "userId") Integer userId) {
        User user = userRepository.findById(userId).get();
        Iterable<Exam> exams = examRepository.findByUserId(user.getId());
        return ResponseEntity.ok(exams);
    }



    @PostMapping("/user/{userId}/exams")
    @PreAuthorize("@userService.hasId(#userId, authentication.name)")
    public ResponseEntity<Exam> createExam(@PathVariable(value = "userId") Integer userId,
                                           @RequestBody Exam exam) {
        return userRepository.findById(userId).map(oldUser -> {
            exam.setUser(oldUser);
            examRepository.save(exam);
            return ResponseEntity.ok(exam);
        }).orElseThrow(() -> new ResourceNotFoundException("UserId " + userId + " not found"));
    }


}


