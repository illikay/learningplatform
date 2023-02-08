package com.kayikci.learningplatform;


import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


@Entity
@Table(name="questions")

public class Question   {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String questionFrage, questionHinweis, questionLoesung;

    private String erstellDatum;

    private String aenderungsDatum;

    private boolean isBeantwortet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exam_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Exam exam;


    public Question() {
    }

    public Question(String questionFrage, String questionHinweis, String questionLoesung, String erstellDatum, String aenderungsDatum, boolean isBeantwortet){

        this.questionFrage = questionFrage;
        this.questionHinweis = questionHinweis;
        this.questionLoesung = questionLoesung;
        this.erstellDatum = erstellDatum;
        this.aenderungsDatum = aenderungsDatum;
        this.isBeantwortet = isBeantwortet;


    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestionFrage() {
        return questionFrage;
    }

    public void setQuestionFrage(String questionFrage) {
        this.questionFrage = questionFrage;
    }

    public String getQuestionHinweis() {
        return questionHinweis;
    }

    public void setQuestionHinweis(String questionHinweis) {
        this.questionHinweis = questionHinweis;
    }

    public String getQuestionLoesung() {
        return questionLoesung;
    }

    public void setQuestionLoesung(String questionLoesung) {
        this.questionLoesung = questionLoesung;
    }

    public String getErstellDatum() {
        return erstellDatum;
    }

    public void setErstellDatum(String erstellDatum) {
        this.erstellDatum = erstellDatum;
    }

    public String getAenderungsDatum() {
        return aenderungsDatum;
    }

    public void setAenderungsDatum(String aenderungsDatum) {
        this.aenderungsDatum = aenderungsDatum;
    }

    public boolean isBeantwortet() {
        return isBeantwortet;
    }

    public void setBeantwortet(boolean beantwortet) {
        isBeantwortet = beantwortet;
    }

    public Exam getExam() {
        return exam;
    }

    public void setExam(Exam exam) {
        this.exam = exam;
    }
}
