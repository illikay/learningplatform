package com.kayikci.learningplatform.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kayikci.learningplatform.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
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
@Table(name = "exams")
public class Exam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Length(max = 20)
    private String pruefungsName;
    @Length(max = 50)
    private String info;
    @Length(max = 50)
    private String beschreibung;
    @Length(max = 20)
    private String erstellDatum;

    @Length(max = 20)
    private String aenderungsDatum;
    @Max(300)
    private int anzahlFragen;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private User user;


    public Exam(String pruefungsName, String info, String beschreibung, String erstellDatum, String aenderungsDatum, int anzahlFragen) {
        this.pruefungsName = pruefungsName;
        this.info = info;
        this.beschreibung = beschreibung;
        this.erstellDatum = erstellDatum;
        this.aenderungsDatum = aenderungsDatum;
        this.anzahlFragen = anzahlFragen;

    }





}
