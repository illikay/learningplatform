package com.kayikci.learningplatform;

import lombok.NonNull;
import org.springframework.http.HttpStatus;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/exam")
public class ExamController {
    @NonNull
    private final ExamRepository repository;
    private final QuestionRepository questionRepository;

    public ExamController(ExamRepository repository, QuestionRepository questionRepository) {
        this.repository = repository;
        this.questionRepository = questionRepository;



    }

    @GetMapping
    Iterable<Exam> getExams() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    Optional<Exam> getExamById(@PathVariable Long id) {
        return repository.findById(id);
    }

    @PostMapping
    Exam postExam(@RequestBody Exam exam) {
        return repository.save(exam);
    }


    @PutMapping("/{id}")
    ResponseEntity<Exam> putExam(@PathVariable Long id, @RequestBody Exam newExam) {
        return repository.findById(id)
                .map(oldExam -> {
                    oldExam.setPruefungsName(newExam.getPruefungsName());
                    oldExam.setInfo(newExam.getInfo());
                    oldExam.setBeschreibung(newExam.getBeschreibung());
                    oldExam.setErstellDatum(newExam.getErstellDatum());
                    oldExam.setAenderungsDatum(newExam.getAenderungsDatum());
                    oldExam.setAnzahlFragen(newExam.getAnzahlFragen());
                    oldExam.setQuestions(newExam.getQuestions());
                    return new ResponseEntity<>(repository.save(oldExam), HttpStatus.OK);

                })
                .orElseGet(() -> {
                    newExam.setId(id);
                    return  new ResponseEntity<>(repository.save(newExam), HttpStatus.CREATED);
                });

    }




    @DeleteMapping("/{id}")
    void deleteExam(@PathVariable Long id) {

        repository.deleteById(id);
    }
}


