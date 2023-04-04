package com.kayikci.learningplatform.data;

import com.kayikci.learningplatform.domain.Exam;
import com.kayikci.learningplatform.domain.Question;
import com.kayikci.learningplatform.repository.ExamRepository;
import com.kayikci.learningplatform.repository.QuestionRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;


@Component
public class DataLoader {

    @Autowired
    private PasswordEncoder passwordEncoder;

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
        Exam exam2 = new Exam("Exam 1", "asdf", "asdf", dateString, dateString, 12);
        Exam exam3 = new Exam("Exam 2", "qwer", "qwer", dateString, dateString, 12);
        Exam exam4 = new Exam("Exam 3", "hjkl", "hjkl", dateString, dateString, 12);
        examRepository.save(exam1);
        examRepository.save(exam2);
        examRepository.save(exam3);
        examRepository.save(exam4);


        Question question1 = new Question("questionFrage123", "questionHinweis", "questionLoesung",
                dateString, dateString, true);

        Question question2 = new Question("questionFrageasdf", "questionHinweis2", "questionLoesung2", dateString, dateString, true);

        question1.setExam(exam1);

        question2.setExam(exam1);
        questionRepository.save(question1);
        questionRepository.save(question2);




    }

}
