package com.kayikci.learningplatform;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.source.InvalidConfigurationPropertyValueException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.kayikci.learningplatform.exception.ResourceNotFoundException;



@RestController
public class QuestionController2 {

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
        return examRepository.findById(examId).map(exam -> {
            question.setExam(exam);
            return questionRepository.save(question);
        }).orElseThrow(() -> new InvalidConfigurationPropertyValueException("Exception", "ExamId " + examId + " not found", "Reason"));
    }

    @PutMapping("/posts/{postId}/comments/{commentId}")
    public Comment updateComment(@PathVariable (value = "postId") Long postId,
                                 @PathVariable (value = "commentId") Long commentId,
                                 @Valid @RequestBody Comment commentRequest) {
        if(!postRepository.existsById(postId)) {
            throw new ResourceNotFoundException("PostId " + postId + " not found");
        }

        return commentRepository.findById(commentId).map(comment -> {
            comment.setText(commentRequest.getText());
            return commentRepository.save(comment);
        }).orElseThrow(() -> new ResourceNotFoundException("CommentId " + commentId + "not found"));
    }

    @DeleteMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable (value = "postId") Long postId,
                                           @PathVariable (value = "commentId") Long commentId) {
        return commentRepository.findByIdAndPostId(commentId, postId).map(comment -> {
            commentRepository.delete(comment);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("Comment not found with id " + commentId + " and postId " + postId));
    }
}