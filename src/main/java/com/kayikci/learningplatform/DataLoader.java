package com.kayikci.learningplatform;


import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class DataLoader {

    //git commit 3
    private final ExamRepository repository;
    private final QuestionRepository questionRepository;

    @Autowired
    private SessionFactory sessionFactory;






    public DataLoader(ExamRepository repository , QuestionRepository questionRepository) {

        this.repository = repository;
        this.questionRepository = questionRepository;

    }

    @PostConstruct
    private void loadData() {
        repository.deleteAll();

        MyDao myDao = new MyDao(sessionFactory);

        SimpleDateFormat format = new SimpleDateFormat("dd.MM.YYYY HH:mm:ss");

        String dateString = format.format( new Date()   );

        Exam exam1 = new Exam( "Prüfung 1", "Info1", "Beschreiung1", dateString, dateString, 12);
        Exam exam2 = new Exam( "Prüfung 2", "Info2", "Beschreiung2", dateString, dateString, 12);
        Exam exam3 = new Exam( "Prüfung 3", "Info3", "Beschreiung3", dateString, dateString, 12);

        //repository.saveAll(List.of(exam1, exam2, exam3));
        myDao.insertExamData(exam1);
        myDao.insertExamData(exam2);
        myDao.insertExamData(exam3);



        Question question1 = new Question("questionFrage", "questionHinweis", "questionLoesung" ,
                dateString, dateString, true, exam1);

        Question question2 = new Question("questionFrage2", "questionHinweis2", "questionLoesung2" , dateString, dateString, true, exam1);

        Question question3 = new Question("questionFrage3", "questionHinweis3", "questionLoesung3" , dateString, dateString, true, exam3);

        //questionRepository.saveAll(List.of(question1, question2, question3));
        myDao.insertQuestionData(question1);
        myDao.insertQuestionData(question2);
        myDao.insertQuestionData(question3);

    }
}
