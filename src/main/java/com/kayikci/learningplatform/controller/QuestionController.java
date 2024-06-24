package com.kayikci.learningplatform.controller;


import com.kayikci.learningplatform.config.JwtService;
import com.kayikci.learningplatform.domain.Exam;
import com.kayikci.learningplatform.repository.ExamRepository;
import com.kayikci.learningplatform.domain.Question;
import com.kayikci.learningplatform.repository.QuestionRepository;
import com.kayikci.learningplatform.exception.ResourceNotFoundException;
import com.kayikci.learningplatform.user.User;
import com.kayikci.learningplatform.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class QuestionController {

    private final ExamRepository examRepository;
    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;
    private final JwtService jwtService;

    @GetMapping("/exam/{examId}/questions")
    @PreAuthorize("@jwtService.isTokenValidForUser(#token, authentication.name)")
    public ResponseEntity<List<Question>> getAllQuestionsByExamId(@RequestHeader("Authorization") String token , @PathVariable(value = "examId") Long examId) {
        List<Question> questions = questionRepository.findByExamId(examId);
        return ResponseEntity.ok(questions);
    }

    @GetMapping("/exam/{examId}/questions/{questionId}")
    @PreAuthorize("@jwtService.isTokenValidForUser(#token, authentication.name)")
    ResponseEntity<Question> getQuestionById(@RequestHeader("Authorization") String token, @PathVariable(value = "examId") Long examId,
                                       @PathVariable(value = "questionId") Long questionId) {
        Question question = questionRepository.findByIdAndExamId(questionId, examId).orElseThrow(() -> new ResourceNotFoundException("Exam not found for examId: " + examId));
        return ResponseEntity.ok(question);
    }


    @PostMapping("/exam/{examId}/questions")
    @PreAuthorize("@jwtService.isTokenValidForUser(#token, authentication.name)")
    public ResponseEntity<Question> createQuestion(@RequestHeader("Authorization") String token, @PathVariable(value = "examId") Long examId,
                                   @RequestBody Question question) {

        Question createdQuestion = examRepository.findById(examId).map(oldExam -> {
            question.setExam(oldExam);
            return questionRepository.save(question);
        }).orElseThrow(() -> new ResourceNotFoundException("ExamId " + examId + " not found"));
        return ResponseEntity.ok(createdQuestion);
    }

    @PutMapping("/exam/{examId}/questions/{questionId}")
    @PreAuthorize("@jwtService.isTokenValidForUser(#token, authentication.name)")
    public ResponseEntity<Question> updateQuestion(@RequestHeader("Authorization") String token, @PathVariable(value = "examId") Long examId,
                                   @PathVariable(value = "questionId") Long questionId,
                                   @RequestBody Question questionRequest) {
        if (!examRepository.existsById(examId)) {
            throw new ResourceNotFoundException("ExamId " + examId + " not found");
        }
        Question updatedQuestion = questionRepository.findById(questionId).map(oldQuestion -> {
            oldQuestion.setQuestionFrage(questionRequest.getQuestionFrage());
            oldQuestion.setQuestionHinweis(questionRequest.getQuestionHinweis());
            oldQuestion.setQuestionLoesung(questionRequest.getQuestionLoesung());
            oldQuestion.setErstellDatum(questionRequest.getErstellDatum());
            oldQuestion.setAenderungsDatum(questionRequest.getAenderungsDatum());
            oldQuestion.setBeantwortet(questionRequest.isBeantwortet());
            return questionRepository.save(oldQuestion);
        }).orElseThrow(() -> new ResourceNotFoundException("QuestionId " + questionId + "not found"));
        return ResponseEntity.ok(updatedQuestion);
    }

    @DeleteMapping("/exam/{examId}/questions/{questionId}")
    @PreAuthorize("@jwtService.isTokenValidForUser(#token, authentication.name)")
    public ResponseEntity<?> deleteQuestion(@RequestHeader("Authorization") String token, @PathVariable(value = "examId") Long examId,
                                            @PathVariable(value = "questionId") Long questionId) {
        return questionRepository.findByIdAndExamId(questionId, examId).map(oldQuestion -> {
            questionRepository.delete(oldQuestion);
            return ResponseEntity.noContent().build();
        }).orElseThrow(() -> new ResourceNotFoundException("Question not found with id " + questionId + " and ExamId " + examId));
    }

}