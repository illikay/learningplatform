package com.kayikci.learningplatform;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;


@Component
public class DataLoader {

    private final ExamRepository examRepository;

    private final QuestionRepository questionRepository;


    public DataLoader(ExamRepository examRepository, QuestionRepository questionRepository) {
        this.examRepository = examRepository;
        this.questionRepository = questionRepository;

    }


    @PostConstruct
    private void loadData() {


        questionRepository.deleteAll();
        examRepository.deleteAll();

        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

        String dateString = format.format(new Date());


        Exam exam1 = new Exam("Prüfung 123", "Info1", "Beschreiung1", dateString, dateString, 12);

        examRepository.save(exam1);

        Question question1 = new Question("questionFrage123", "questionHinweis", "questionLoesung",
                dateString, dateString, true);

        Question question2 = new Question("questionFrageasdf", "questionHinweis2", "questionLoesung2", dateString, dateString, true);

        question1.setExam(exam1);

        question2.setExam(exam1);
        questionRepository.save(question1);
        questionRepository.save(question2);
    }

}
