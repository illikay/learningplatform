package com.kayikci.learningplatform;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.source.InvalidConfigurationPropertyValueException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;





@RestController
public class QuestionController {

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @GetMapping("/exam/{examId}/questions")
    public Page<Question> getAllQuestionsByExamId(@PathVariable (value = "examId") Long examId,
                                                Pageable pageable) {
        return questionRepository.findByExamId(examId, pageable);
    }



    @PostMapping("/exam/{examId}/questions")
    public Question createQuestion(@PathVariable (value = "examId") Long examId,
                                 @RequestBody Question question) {
        return examRepository.findById(examId).map(oldExam -> {
            question.setExam(oldExam);
            oldExam.getQuestions().add(question);
            examRepository.save(oldExam);
            return question;
        }).orElseThrow(() -> new InvalidConfigurationPropertyValueException("Exception", "ExamId " + examId + " not found", "Reason"));
    }

    @PutMapping("/exam/{examId}/questions/{questionId}")
    public Question updateQuestion(@PathVariable (value = "examId") Long examId,
                                 @PathVariable (value = "questionId") Long questionId,
                                 @RequestBody Question questionRequest) {
        if(!examRepository.existsById(examId)) {
            throw new InvalidConfigurationPropertyValueException("Exception", "ExamId " + examId + " not found", "Reason");
        }

        return questionRepository.findById(questionId).map(oldQuestion -> {
            oldQuestion.setQuestionFrage(questionRequest.getQuestionFrage());
            oldQuestion.setQuestionHinweis(questionRequest.getQuestionHinweis());
            oldQuestion.setQuestionLoesung(questionRequest.getQuestionLoesung());
            oldQuestion.setErstellDatum(questionRequest.getErstellDatum());
            oldQuestion.setAenderungsDatum(questionRequest.getAenderungsDatum());
            oldQuestion.setBeantwortet(questionRequest.isBeantwortet());
            return questionRepository.save(oldQuestion);
        }).orElseThrow(() -> new InvalidConfigurationPropertyValueException("Exception", "QuestionId " + questionId + " not found", "Reason"));
    }

    @DeleteMapping("/exam/{examId}/questions/{questionId}")
    public ResponseEntity<?> deleteQuestion(@PathVariable (value = "examId") Long examId,
                                           @PathVariable (value = "questionId") Long questionId) {
        return questionRepository.findByIdAndExamId(questionId, examId).map(oldQuestion -> {
            questionRepository.delete(oldQuestion);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new InvalidConfigurationPropertyValueException("Exception", "Comment not found with id " + questionId + " and postId " + examId, "Reason"));
    }
}