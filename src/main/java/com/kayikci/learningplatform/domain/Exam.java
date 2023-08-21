package com.kayikci.learningplatform.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @Column(unique = true)
    private String pruefungsName;
    @Length(max = 50)
    private String info;
    @Length(max = 50)
    private String beschreibung;


    @Pattern(regexp = "^(0[1-9]|[12][0-9]|3[01])\\.(0[1-9]|1[0-2])\\.\\d{4}$", message = "Ungültiges Datum eingegeben.")
    private String erstellDatum;


    @Pattern(regexp = "^(0[1-9]|[12][0-9]|3[01])\\.(0[1-9]|1[0-2])\\.\\d{4}$", message = "Ungültiges Datum eingegeben.")
    private String aenderungsDatum;

    @Max(300)
    @Min(0)
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
