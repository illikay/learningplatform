package com.kayikci.learningplatform;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslJpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
public class QuestionController {

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private QuestionRepository questionRepository;


    @GetMapping("/exam/{examId}/questions")
    public Iterable<Question>  getAllQuestionsByExamId(@PathVariable (value = "examId") Long examId) {
        return questionRepository.findByExamId(examId);
    }

    @GetMapping("/exam/{examId}/questions/{questionId}")
    Optional<Question> getQuestionById(@PathVariable (value = "examId") Long examId,
                               @PathVariable (value = "questionId") Long questionId) {
        return questionRepository.findByIdAndExamId(questionId, examId);
    }



    @PostMapping("/exam/{examId}/questions")
    public Question createQuestion(@PathVariable (value = "examId") Long examId,
                                 @RequestBody Question question) {
        return examRepository.findById(examId).map(oldExam -> {
            question.setExam(oldExam);
            return questionRepository.save(question);
        }).orElseThrow(() -> new ResourceNotFoundException("ExamId " + examId + " not found"));
    }

    @PutMapping("/exam/{examId}/questions/{questionId}")
    public Question updateQuestion(@PathVariable (value = "examId") Long examId,
                                 @PathVariable (value = "questionId") Long questionId,
                                 @RequestBody Question questionRequest) {
        if(!examRepository.existsById(examId)) {
            throw new ResourceNotFoundException("ExamId " + examId + " not found");       }

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
    public ResponseEntity<?> deleteQuestion(@PathVariable (value = "examId") Long examId,
                                           @PathVariable (value = "questionId") Long questionId) {
        return questionRepository.findByIdAndExamId(questionId, examId).map(oldQuestion -> {
            questionRepository.delete(oldQuestion);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("Question not found with id " + questionId + " and ExamId " + examId));
    }

}