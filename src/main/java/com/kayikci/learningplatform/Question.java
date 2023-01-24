package com.kayikci.learningplatform;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


@Entity
@Data
@Table(name="Question")
@NoArgsConstructor
@AllArgsConstructor
public class Question   {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long questionId;


    private String questionFrage, questionHinweis, questionLoesung;

    private String erstellDatum;

    private String aenderungsDatum;

    private boolean isBeantwortet;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="exam_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
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
