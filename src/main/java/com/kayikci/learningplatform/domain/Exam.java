package com.kayikci.learningplatform.domain;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.ZonedDateTimeSerializer;
import com.kayikci.learningplatform.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "exams")
public class Exam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Length(max = 20)
    private String pruefungsName;
    @Length(max = 50)
    private String info;
    @Length(max = 50)
    private String beschreibung;



    @JsonSerialize(using = CustomZonedDateTimeSerializer.class)
    @JsonDeserialize(using = CustomZonedDateTimeDeserializer.class)
    private ZonedDateTime erstellDatum;


    @JsonSerialize(using = CustomZonedDateTimeSerializer.class)
    @JsonDeserialize(using = CustomZonedDateTimeDeserializer.class)
    private ZonedDateTime aenderungsDatum;

    @Max(300)
    @Min(0)
    private int anzahlFragen;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private User user;


    public Exam(String pruefungsName, String info, String beschreibung, ZonedDateTime erstellDatum, ZonedDateTime aenderungsDatum, int anzahlFragen) {
        this.pruefungsName = pruefungsName;
        this.info = info;
        this.beschreibung = beschreibung;
        this.erstellDatum = erstellDatum;
        this.aenderungsDatum = aenderungsDatum;
        this.anzahlFragen = anzahlFragen;

    }





}
