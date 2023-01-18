package com.kayikci.learningplatform;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;




@Entity
@Data
@Table(name="Question")
@NoArgsConstructor
@AllArgsConstructor
public class Question   {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String questionFrage, questionHinweis, questionLoesung;

    private String erstellDatum;

    private String aenderungsDatum;

    private boolean isBeantwortet;

    @ManyToOne
    @JoinColumn(name="Exam_id")
    private Exam exam;



    public Question(String questionFrage, String questionHinweis, String questionLoesung, String erstellDatum, String aenderungsDatum, boolean isBeantwortet, Exam exam){
        this.questionFrage = questionFrage;
        this.questionHinweis = questionHinweis;
        this.questionLoesung = questionLoesung;
        this.erstellDatum = erstellDatum;
        this.aenderungsDatum = aenderungsDatum;
        this.isBeantwortet = isBeantwortet;
        this.exam = exam;

    }
}
