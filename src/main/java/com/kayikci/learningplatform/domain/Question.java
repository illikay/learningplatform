package com.kayikci.learningplatform.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kayikci.learningplatform.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "questions")

public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Length(max = 50)
    @Column(unique = true)
    private String questionFrage;
    @Length(max = 50)
    private String  questionHinweis;

    @Length(max = 50)
    private String questionLoesung;


    @Pattern(regexp = "^(0[1-9]|[12][0-9]|3[01])\\.(0[1-9]|1[0-2])\\.\\d{4}$", message = "Ungültiges Datum eingegeben.")
    private String erstellDatum;


    @Pattern(regexp = "^(0[1-9]|[12][0-9]|3[01])\\.(0[1-9]|1[0-2])\\.\\d{4}$", message = "Ungültiges Datum eingegeben.")
    private String aenderungsDatum;

    private boolean isBeantwortet;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exam_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Exam exam;

    public Question(String questionFrage, String questionHinweis, String questionLoesung, String erstellDatum, String aenderungsDatum, boolean isBeantwortet) {

        this.questionFrage = questionFrage;
        this.questionHinweis = questionHinweis;
        this.questionLoesung = questionLoesung;
        this.erstellDatum = erstellDatum;
        this.aenderungsDatum = aenderungsDatum;
        this.isBeantwortet = isBeantwortet;


    }





}
