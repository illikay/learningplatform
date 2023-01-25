package com.kayikci.learningplatform;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Entity
@Data
@Table(name="Exam")
@NoArgsConstructor
@AllArgsConstructor
public class Exam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String pruefungsName, info, beschreibung;
    private String erstellDatum;

    private String aenderungsDatum;

    private int anzahlFragen;





    public Exam(String pruefungsName, String info, String beschreibung, String erstellDatum, String aenderungsDatum, int anzahlFragen) {
        this.pruefungsName = pruefungsName;
        this.info = info;
        this.beschreibung = beschreibung;
        this.erstellDatum = erstellDatum;
        this.aenderungsDatum = aenderungsDatum;
        this.anzahlFragen = anzahlFragen;

    }
}
