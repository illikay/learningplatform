package com.kayikci.learningplatform.controller;


import com.kayikci.learningplatform.repository.ExamRepository;
import com.kayikci.learningplatform.domain.Question;
import com.kayikci.learningplatform.repository.QuestionRepository;
import com.kayikci.learningplatform.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class QuestionController {

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private QuestionRepository questionRepository;


    @GetMapping("/exam/{examId}/questions")
    @PreAuthorize("@userService.hasId(#userId, authentication.name)")
    public Iterable<Question> getAllQuestionsByExamId(@PathVariable(value= "userId") Integer userId , @PathVariable(value = "examId") Long examId) {
        return questionRepository.findByExamId(examId);
    }

    @GetMapping("/exam/{examId}/questions/{questionId}")
    Optional<Question> getQuestionById(@PathVariable(value = "examId") Long examId,
                                       @PathVariable(value = "questionId") Long questionId) {
        return questionRepository.findByIdAndExamId(questionId, examId);
    }


    @PostMapping("/exam/{examId}/questions")
    public Question createQuestion(@PathVariable(value = "examId") Long examId,
                                   @RequestBody Question question) {
        return examRepository.findById(examId).map(oldExam -> {
            question.setExam(oldExam);
            return questionRepository.save(question);
        }).orElseThrow(() -> new ResourceNotFoundException("ExamId " + examId + " not found"));
    }

    @PutMapping("/exam/{examId}/questions/{questionId}")
    public Question updateQuestion(@PathVariable(value = "examId") Long examId,
                                   @PathVariable(value = "questionId") Long questionId,
                                   @RequestBody Question questionRequest) {
        if (!examRepository.existsById(examId)) {
            throw new ResourceNotFoundException("ExamId " + examId + " not found");
        }

        return questionRepository.findById(questionId).map(oldQuestion -> {
            oldQuestion.setQuestionFrage(questionRequest.getQuestionFrage());
            oldQuestion.setQuestionHinweis(questionRequest.getQuestionHinweis());
            oldQuestion.setQuestionLoesung(questionRequest.getQuestionLoesung());
            oldQuestion.setErstellDatum(questionRequest.getErstellDatum());
            oldQuestion.setAenderungsDatum(questionRequest.getAenderungsDatum());
            oldQuestion.setBeantwortet(questionRequest.isBeantwortet());
            return questionRepository.save(oldQuestion);
        }).orElseThrow(() -> new ResourceNotFoundException("QuestionId " + questionId + "not found"));
    }

    @DeleteMapping("/exam/{examId}/questions/{questionId}")
    public ResponseEntity<?> deleteQuestion(@PathVariable(value = "examId") Long examId,
                                            @PathVariable(value = "questionId") Long questionId) {
        return questionRepository.findByIdAndExamId(questionId, examId).map(oldQuestion -> {
            questionRepository.delete(oldQuestion);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("Question not found with id " + questionId + " and ExamId " + examId));
    }

}