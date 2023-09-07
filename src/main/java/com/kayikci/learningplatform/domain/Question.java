package com.kayikci.learningplatform.domain;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
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

import java.time.LocalDate;
import java.time.LocalDateTime;

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



    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime erstellDatum;



    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime aenderungsDatum;

    private boolean isBeantwortet;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exam_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Exam exam;

    public Question(String questionFrage, String questionHinweis, String questionLoesung, LocalDateTime erstellDatum, LocalDateTime aenderungsDatum, boolean isBeantwortet) {

        this.questionFrage = questionFrage;
        this.questionHinweis = questionHinweis;
        this.questionLoesung = questionLoesung;
        this.erstellDatum = erstellDatum;
        this.aenderungsDatum = aenderungsDatum;
        this.isBeantwortet = isBeantwortet;


    }







}
