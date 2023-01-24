package com.kayikci.learningplatform;

import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/exam")
public class ExamController {
    @NonNull
    private final ExamRepository repository;


    public ExamController(ExamRepository repository, QuestionRepository questionRepository) {
        this.repository = repository;



    }

    @GetMapping
    public Page<Exam> getAllPosts(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @GetMapping("/{id}")
    Optional<Exam> getExamById(@PathVariable Long examId) {
        return repository.findById(examId);
    }

    @PostMapping
    Exam postExam(@RequestBody Exam exam) {
        return repository.save(exam);
    }


    @PutMapping("/{id}")
    ResponseEntity<Exam> putExam(@PathVariable Long examId, @RequestBody Exam newExam) {
        return repository.findById(examId)
                .map(oldExam -> {
                    oldExam.setPruefungsName(newExam.getPruefungsName());
                    oldExam.setInfo(newExam.getInfo());
                    oldExam.setBeschreibung(newExam.getBeschreibung());
                    oldExam.setErstellDatum(newExam.getErstellDatum());
                    oldExam.setAenderungsDatum(newExam.getAenderungsDatum());
                    oldExam.setAnzahlFragen(newExam.getAnzahlFragen());
                    return new ResponseEntity<>(repository.save(oldExam), HttpStatus.OK);

                })
                .orElseGet(() -> {
                    newExam.setExamId(examId);
                    return  new ResponseEntity<>(repository.save(newExam), HttpStatus.CREATED);
                });

    }




    @DeleteMapping("/{id}")
    void deleteExam(@PathVariable Long examId) {

        repository.deleteById(examId);
    }
}


