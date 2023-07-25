package com.kayikci.learningplatform;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kayikci.learningplatform.auth.AuthenticationRequest;
import com.kayikci.learningplatform.auth.AuthenticationResponse;
import com.kayikci.learningplatform.auth.AuthenticationService;
import com.kayikci.learningplatform.auth.RegisterRequest;
import com.kayikci.learningplatform.config.JwtService;
import com.kayikci.learningplatform.domain.Exam;
import com.kayikci.learningplatform.repository.ExamRepository;
import com.kayikci.learningplatform.token.TokenRepository;
import com.kayikci.learningplatform.user.Role;
import com.kayikci.learningplatform.user.User;
import com.kayikci.learningplatform.user.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ExamControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ExamRepository examRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private AuthenticationService authenticationService;

    @BeforeAll
    static void beforeAllTests() {

    }

    @BeforeEach
    public void setUp() {
        examRepository.deleteAll();
        tokenRepository.deleteAll();
        userRepository.deleteAll();


    }

    @AfterEach
    public void afterSetup() {
        examRepository.deleteAll();
        tokenRepository.deleteAll();
        userRepository.deleteAll();

    }

    @Test
    public void testGetExams() throws Exception {
        // Given

        RegisterRequest registerRequest = new RegisterRequest("firstname","lastname", "asdf@asdf.de", "Asdf0101!");

        AuthenticationResponse  authenticationResponse = authenticationService.register(registerRequest);
        String token = authenticationResponse.getToken();

        Exam exam1 = new Exam("pruefungsname1", "info1",
                "beschreibung1", "erstelldatum1", "aenderungsdatum1", 12);
        User user = userRepository.findByEmail(registerRequest.getEmail()).get();

        exam1.setUser(user);
        examRepository.save(exam1);




        // When
        mockMvc.perform(get("/exam")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].pruefungsName").value("pruefungsname1"))
                .andExpect(jsonPath("$[0].info").value("info1"))
                .andExpect(jsonPath("$[0].beschreibung").value("beschreibung1"))
                .andExpect(jsonPath("$[0].erstellDatum").value("erstelldatum1"))
                .andExpect(jsonPath("$[0].aenderungsDatum").value("aenderungsdatum1"))
                .andExpect(jsonPath("$[0].erstellDatum").value("erstelldatum1"))
                .andExpect(jsonPath("$[0].anzahlFragen").value(12));
    }

    @Test
    public void testGetExamById() throws Exception {

        // Given

        RegisterRequest registerRequest = new RegisterRequest("firstname","lastname", "asdf@asdf.de", "Asdf0101!");

        AuthenticationResponse  authenticationResponse = authenticationService.register(registerRequest);
        String token = authenticationResponse.getToken();

        Exam exam1 = new Exam("pruefungsname1", "info1",
                "beschreibung1", "erstelldatum1", "aenderungsdatum1", 12);
        User user = userRepository.findByEmail(registerRequest.getEmail()).get();

        exam1.setUser(user);
        examRepository.save(exam1);



        // When
        mockMvc.perform(get("/exam/{examId}", exam1.getId())
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pruefungsName").value("pruefungsname1"))
                .andExpect(jsonPath("$.info").value("info1"))
                .andExpect(jsonPath("$.beschreibung").value("beschreibung1"))
                .andExpect(jsonPath("$.erstellDatum").value("erstelldatum1"))
                .andExpect(jsonPath("$.aenderungsDatum").value("aenderungsdatum1"))
                .andExpect(jsonPath("$.anzahlFragen").value(12));
    }

    @Test
    public void testCreateExam() throws Exception {
        // Given

        RegisterRequest registerRequest = new RegisterRequest("firstname","lastname", "asdf@asdf.de", "Asdf0101!");

        AuthenticationResponse  authenticationResponse = authenticationService.register(registerRequest);
        String token = authenticationResponse.getToken();

        Exam exam1 = new Exam("pruefungsname1", "info1",
                "beschreibung1", "erstelldatum1", "aenderungsdatum1", 12);
        User user = userRepository.findByEmail(registerRequest.getEmail()).get();

        exam1.setUser(user);
        examRepository.save(exam1);

        // When
        mockMvc.perform(post("/exam")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(exam1))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pruefungsName").value("pruefungsname1"))
                .andExpect(jsonPath("$.info").value("info1"))
                .andExpect(jsonPath("$.beschreibung").value("beschreibung1"))
                .andExpect(jsonPath("$.erstellDatum").value("erstelldatum1"))
                .andExpect(jsonPath("$.aenderungsDatum").value("aenderungsdatum1"))
                .andExpect(jsonPath("$.anzahlFragen").value(12));

        // Then
        Exam savedExam = examRepository.findById(exam1.getId()).get();
        assertThat(savedExam.getPruefungsName()).isEqualTo(exam1.getPruefungsName());
        assertThat(savedExam.getInfo()).isEqualTo(exam1.getInfo());
        assertThat(savedExam.getBeschreibung()).isEqualTo(exam1.getBeschreibung());
        assertThat(savedExam.getErstellDatum()).isEqualTo(exam1.getErstellDatum());
        assertThat(savedExam.getAenderungsDatum()).isEqualTo(exam1.getAenderungsDatum());
        assertThat(savedExam.getAnzahlFragen()).isEqualTo(exam1.getAnzahlFragen());
    }

    @Test
    public void testUpdateExam() throws Exception {
        // Given

        RegisterRequest registerRequest = new RegisterRequest("firstname","lastname", "asdf@asdf.de", "Asdf0101!");

        AuthenticationResponse  authenticationResponse = authenticationService.register(registerRequest);
        String token = authenticationResponse.getToken();

        Exam exam1 = new Exam("pruefungsname1", "info1",
                "beschreibung1", "erstelldatum1", "aenderungsdatum1", 12);
        User user = userRepository.findByEmail(registerRequest.getEmail()).get();

        exam1.setUser(user);
        examRepository.save(exam1);

        exam1.setPruefungsName("pruefungsname2");
        exam1.setInfo("info2");
        exam1.setBeschreibung("beschreibung2");
        exam1.setErstellDatum("erstelldatum2");
        exam1.setAenderungsDatum("aenderungsdatum2");
        exam1.setAnzahlFragen(13);

        // When
        mockMvc.perform(put("/exam/{examId}", exam1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(exam1))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pruefungsName").value("pruefungsname2"))
                .andExpect(jsonPath("$.info").value("info2"))
                .andExpect(jsonPath("$.beschreibung").value("beschreibung2"))
                .andExpect(jsonPath("$.erstellDatum").value("erstelldatum2"))
                .andExpect(jsonPath("$.aenderungsDatum").value("aenderungsdatum2"))
                .andExpect(jsonPath("$.anzahlFragen").value(13));

        // Then
        Exam updatedExam = examRepository.findById(exam1.getId()).get();
        assertThat(updatedExam.getPruefungsName()).isEqualTo(exam1.getPruefungsName());
        assertThat(updatedExam.getInfo()).isEqualTo(exam1.getInfo());
        assertThat(updatedExam.getBeschreibung()).isEqualTo(exam1.getBeschreibung());
        assertThat(updatedExam.getErstellDatum()).isEqualTo(exam1.getErstellDatum());
        assertThat(updatedExam.getAenderungsDatum()).isEqualTo(exam1.getAenderungsDatum());
        assertThat(updatedExam.getAnzahlFragen()).isEqualTo(exam1.getAnzahlFragen());
    }

    @Test
    public void testDeleteExam() throws Exception {
        // Given

        RegisterRequest registerRequest = new RegisterRequest("firstname","lastname", "asdf@asdf.de", "Asdf0101!");

        AuthenticationResponse  authenticationResponse = authenticationService.register(registerRequest);
        String token = authenticationResponse.getToken();

        Exam exam1 = new Exam("pruefungsname1", "info1",
                "beschreibung1", "erstelldatum1", "aenderungsdatum1", 12);
        User user = userRepository.findByEmail(registerRequest.getEmail()).get();

        exam1.setUser(user);
        examRepository.save(exam1);

        // When
        mockMvc.perform(delete("/exam/{examId}", exam1.getId())
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());

        // Then
        assertThat(examRepository.findById(exam1.getId())).isEmpty();
    }
}