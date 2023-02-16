package com.kayikci.learningplatform;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/exam")
public class ExamController {

    private final ExamRepository examRepository;
    private final QuestionRepository questionRepository;


    public ExamController(ExamRepository examRepository, QuestionRepository questionRepository, QuestionRepository questionRepository1) {
        this.examRepository = examRepository;
        this.questionRepository = questionRepository1;
    }

    @GetMapping
    public Iterable<Exam> getExams() {
        return examRepository.findAll();
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
    public Exam updateExam(@PathVariable Long examId, @RequestBody Exam newExam) {
        return examRepository.findById(examId)
                .map(oldExam -> {
                    oldExam.setPruefungsName(newExam.getPruefungsName());
                    oldExam.setInfo(newExam.getInfo());
                    oldExam.setBeschreibung(newExam.getBeschreibung());
                    oldExam.setErstellDatum(newExam.getErstellDatum());
                    oldExam.setAenderungsDatum(newExam.getAenderungsDatum());
                    oldExam.setAnzahlFragen(newExam.getAnzahlFragen());
                    return examRepository.save(oldExam);
                }).orElseThrow(() -> new ResourceNotFoundException("ExamId " + examId + " not found"));
    }


    @DeleteMapping("/{examId}")
    public ResponseEntity<?> deleteExam(@PathVariable Long examId) {
        questionRepository.deleteAll(questionRepository.findByExamId(examId));
        return examRepository.findById(examId).map(oldExam -> {
            examRepository.delete(oldExam);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("ExamId " + examId + " not found"));
    }
}


