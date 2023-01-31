package com.kayikci.learningplatform;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="exams")
public class Exam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String pruefungsName, info, beschreibung;
    private String erstellDatum;

    private String aenderungsDatum;

    private int anzahlFragen;




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


}
