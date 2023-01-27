package com.kayikci.learningplatform;

import lombok.NonNull;
import org.springframework.boot.context.properties.source.InvalidConfigurationPropertyValueException;
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
    private final ExamRepository examRepository;


    public ExamController(ExamRepository examRepository, QuestionRepository questionRepository) {
        this.examRepository = examRepository;



    }

    @GetMapping
    public Page<Exam> getAllPosts(Pageable pageable) {
        return examRepository.findAll(pageable);
    }

    @GetMapping("/{examId}")
    Optional<Exam> getExamById(@PathVariable Long examId) {
        return examRepository.findById(examId);
    }

    @PostMapping
    Exam postExam(@RequestBody Exam exam) {
        return examRepository.save(exam);
    }


    @PutMapping("/{examId}")
    ResponseEntity<Exam> putExam(@PathVariable Long examId, @RequestBody Exam newExam) {
        return examRepository.findById(examId)
                .map(oldExam -> {
                    oldExam.setPruefungsName(newExam.getPruefungsName());
                    oldExam.setInfo(newExam.getInfo());
                    oldExam.setBeschreibung(newExam.getBeschreibung());
                    oldExam.setErstellDatum(newExam.getErstellDatum());
                    oldExam.setAenderungsDatum(newExam.getAenderungsDatum());
                    oldExam.setAnzahlFragen(newExam.getAnzahlFragen());
                    return new ResponseEntity<>(examRepository.save(oldExam), HttpStatus.OK);

                })
                .orElseGet(() -> {
                    newExam.setId(examId);
                    return  new ResponseEntity<>(examRepository.save(newExam), HttpStatus.CREATED);
                });

    }




    @DeleteMapping("/{examId}")
    public ResponseEntity<?> deleteExam(@PathVariable Long examId) {
        return examRepository.findById(examId).map(post -> {
            examRepository.delete(post);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new InvalidConfigurationPropertyValueException("Exception", "ExamId " + examId + " not found", "Reason"));
    }
}


