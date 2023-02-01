package com.kayikci.learningplatform;



import org.springframework.stereotype.Component;


import jakarta.annotation.PostConstruct;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;


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

        SimpleDateFormat format = new SimpleDateFormat("dd.MM.YYYY HH:mm:ss");

        String dateString = format.format( new Date()   );


        Exam exam1 = new Exam("Prüfung 123", "Info1", "Beschreiung1", dateString, dateString, 12);

        examRepository.save(exam1);

        Question question1 = new Question("questionFrage123", "questionHinweis", "questionLoesung" ,
                dateString, dateString, true);

        Question question2 = new Question( "questionFrageasdf", "questionHinweis2", "questionLoesung2" , dateString, dateString, true);

        question1.setExam(exam1);

        question2.setExam(exam1);
        questionRepository.save(question1);
        questionRepository.save(question2);







      /*  Exam exam2 = new Exam("Prüfung 2", "Info2", "Beschreiung2", dateString, dateString, 12);
        Exam exam3 = new Exam("Prüfung 3", "Info3", "Beschreiung3", dateString, dateString, 12);




        Question question2 = new Question( "questionFrage2", "questionHinweis2", "questionLoesung2" , dateString, dateString, true);

        Question question3 = new Question( "questionFrage3", "questionHinweis3", "questionLoesung3" , dateString, dateString, true);

        question1.setExam(exam1);
        question2.setExam(exam1);
        question3.setExam(exam3);



        examRepository.saveAll(List.of(exam1,exam2,exam3)); */

    }
}
