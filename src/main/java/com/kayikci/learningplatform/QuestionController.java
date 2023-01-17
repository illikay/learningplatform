package com.kayikci.learningplatform;

import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/question")
public class QuestionController {
    @NonNull
    private final QuestionRepository repository;

    public QuestionController(QuestionRepository repository) {
        this.repository = repository;



    }

    @GetMapping
    Iterable<Question> getQuestions() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    Optional<Question> getQuestionById(@PathVariable Long id) {
        return repository.findById(id);
    }

    @PostMapping
    Question postQuestion(@RequestBody Question question) {
        return repository.save(question);
    }


    @PutMapping("/{id}")
    ResponseEntity<Question> putQuestion(@PathVariable Long id, @RequestBody Question newQuestion) {
        return repository.findById(id)
                .map(oldQuestion -> {
                    oldQuestion.setQuestionFrage(newQuestion.getQuestionFrage());
                    oldQuestion.setQuestionHinweis(newQuestion.getQuestionHinweis());
                    oldQuestion.setQuestionLoesung(newQuestion.getQuestionLoesung());
                    oldQuestion.setErstellDatum(newQuestion.getErstellDatum());
                    oldQuestion.setAenderungsDatum(newQuestion.getAenderungsDatum());
                    oldQuestion.setBeantwortet(newQuestion.isBeantwortet());
                    oldQuestion.setExam(newQuestion.getExam());
                    return new ResponseEntity<>(repository.save(oldQuestion), HttpStatus.OK);

                })
                .orElseGet(() -> {
                    newQuestion.setId(id);
                    return  new ResponseEntity<>(repository.save(newQuestion), HttpStatus.CREATED);
                });

    }




    @DeleteMapping("/{id}")
    void deleteQuestion(@PathVariable Long id) {

        repository.deleteById(id);
    }
}

