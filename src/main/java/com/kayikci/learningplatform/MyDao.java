package com.kayikci.learningplatform;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public class MyDao {

    private final SessionFactory sessionFactory;

    @Autowired
    public MyDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    public void insertExamData(Exam exam) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(exam);
        session.getTransaction().commit();
        session.close();
    }

    public void insertQuestionData(Question question) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(question);
        session.getTransaction().commit();
        session.close();
    }
}
