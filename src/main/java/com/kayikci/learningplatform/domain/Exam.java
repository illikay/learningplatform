package com.kayikci.learningplatform.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kayikci.learningplatform.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.Length;


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


    public Exam() {
    }

    public Exam(String pruefungsName, String info, String beschreibung, String erstellDatum, String aenderungsDatum, int anzahlFragen) {
        this.pruefungsName = pruefungsName;
        this.info = info;
        this.beschreibung = beschreibung;
        this.erstellDatum = erstellDatum;
        this.aenderungsDatum = aenderungsDatum;
        this.anzahlFragen = anzahlFragen;

    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPruefungsName() {
        return pruefungsName;
    }

    public void setPruefungsName(String pruefungsName) {
        this.pruefungsName = pruefungsName;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
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

    public int getAnzahlFragen() {
        return anzahlFragen;
    }

    public void setAnzahlFragen(int anzahlFragen) {
        this.anzahlFragen = anzahlFragen;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
