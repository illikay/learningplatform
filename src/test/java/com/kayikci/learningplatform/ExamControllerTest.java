package com.kayikci.learningplatform;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.kayikci.learningplatform.auth.AuthenticationResponse;
import com.kayikci.learningplatform.auth.AuthenticationService;
import com.kayikci.learningplatform.auth.RegisterRequest;

import com.kayikci.learningplatform.domain.Exam;
import com.kayikci.learningplatform.exception.ResourceNotFoundException;
import com.kayikci.learningplatform.repository.ExamRepository;
import com.kayikci.learningplatform.token.TokenRepository;

import com.kayikci.learningplatform.user.User;
import com.kayikci.learningplatform.user.UserRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;


import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;


import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
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

    @Autowired
    private TestRestTemplate testRestTemplate;

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
    public void testAuthorization() {

        RegisterRequest registerRequest = new RegisterRequest("firstname","lastname", "asdf@asdf.de", "Asdf0101!");

        AuthenticationResponse  authenticationResponse = authenticationService.register(registerRequest);
        String token = authenticationResponse.getToken();

        Exam exam1 = new Exam("pruefungsname1", "info1",
                "beschreibung1", "12.08.2023", "12.08.2023", 12);

        User user = userRepository.findByEmail(registerRequest.getEmail()).orElseThrow(() ->
                new ResourceNotFoundException("User not found for email:" + registerRequest.getEmail()));

        exam1.setUser(user);
        examRepository.save(exam1);

        HttpHeaders headers = new HttpHeaders();
        headers.set("accept", "application/json");
        headers.set("Authorization", "Bearer " + token);
        HttpEntity requestEntity = new HttpEntity<>(null, headers);

        ResponseEntity<List<Exam>> response = testRestTemplate.exchange("/exam",
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<>() {
                });
        List<Exam> exams = response.getBody();
        // Assert
        assertTrue(response.getStatusCode().is2xxSuccessful(), "HTTP Response status code should be 200");
        assertNotNull(exams, "The list exams shouldn't be null");
        assertEquals(1, exams.size(), "There should be exactly 1 user in the list");


    }

    @Test
    public void testGetExams() throws Exception {
        // Given

        RegisterRequest registerRequest = new RegisterRequest("firstname","lastname", "asdf@asdf.de", "Asdf0101!");

        AuthenticationResponse  authenticationResponse = authenticationService.register(registerRequest);
        String token = authenticationResponse.getToken();

        Exam exam1 = new Exam("pruefungsname1", "info1",
                "beschreibung1", "12.08.2023", "12.08.2023", 12);
        User user = userRepository.findByEmail(registerRequest.getEmail()).orElseThrow(() ->
                new ResourceNotFoundException("User not found for email:" + registerRequest.getEmail()));

        exam1.setUser(user);
        examRepository.save(exam1);




        // When
        mockMvc.perform(get("/exam")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].pruefungsName").value("pruefungsname1"))
                .andExpect(jsonPath("$[0].info").value("info1"))
                .andExpect(jsonPath("$[0].beschreibung").value("beschreibung1"))
                .andExpect(jsonPath("$[0].erstellDatum").value("12.08.2023"))
                .andExpect(jsonPath("$[0].aenderungsDatum").value("12.08.2023"))
                .andExpect(jsonPath("$[0].anzahlFragen").value(12));
    }

    @Test
    public void testGetExamById() throws Exception {

        // Given

        RegisterRequest registerRequest = new RegisterRequest("firstname","lastname", "asdf@asdf.de", "Asdf0101!");

        AuthenticationResponse  authenticationResponse = authenticationService.register(registerRequest);
        String token = authenticationResponse.getToken();

        Exam exam1 = new Exam("pruefungsname1", "info1",
                "beschreibung1", "12.08.2023", "12.08.2023", 12);
        User user = userRepository.findByEmail(registerRequest.getEmail()).orElseThrow(() ->
                new ResourceNotFoundException("User not found for email:" + registerRequest.getEmail()));

        exam1.setUser(user);
        examRepository.save(exam1);



        // When
        mockMvc.perform(get("/exam/{examId}", exam1.getId())
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pruefungsName").value("pruefungsname1"))
                .andExpect(jsonPath("$.info").value("info1"))
                .andExpect(jsonPath("$.beschreibung").value("beschreibung1"))
                .andExpect(jsonPath("$.erstellDatum").value("12.08.2023"))
                .andExpect(jsonPath("$.aenderungsDatum").value("12.08.2023"))
                .andExpect(jsonPath("$.anzahlFragen").value(12));
    }

    @Test
    public void testCreateExam() throws Exception {
        // Given

        RegisterRequest registerRequest = new RegisterRequest("firstname","lastname", "asdf@asdf.de", "Asdf0101!");

        AuthenticationResponse  authenticationResponse = authenticationService.register(registerRequest);
        String token = authenticationResponse.getToken();

        Exam exam1 = new Exam("pruefungsname1", "info1",
                "beschreibung1", "12.08.2023", "12.08.2023", 12);
        User user = userRepository.findByEmail(registerRequest.getEmail()).orElseThrow(() ->
                new ResourceNotFoundException("User not found for email:" + registerRequest.getEmail()));

        exam1.setUser(user);
        //examRepository.save(exam1);

        // When
        mockMvc.perform(post("/exam")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(exam1))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pruefungsName").value("pruefungsname1"))
                .andExpect(jsonPath("$.info").value("info1"))
                .andExpect(jsonPath("$.beschreibung").value("beschreibung1"))
                .andExpect(jsonPath("$.erstellDatum").value("12.08.2023"))
                .andExpect(jsonPath("$.aenderungsDatum").value("12.08.2023"))
                .andExpect(jsonPath("$.anzahlFragen").value(12));

        Exam savedExam = examRepository.findByPruefungsName(exam1.getPruefungsName()).orElseThrow(() ->
                new ResourceNotFoundException("Exam not found for pruefungsName: " + exam1.getPruefungsName()));


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
                "beschreibung1", "12.08.2023", "12.08.2023", 12);
        User user = userRepository.findByEmail(registerRequest.getEmail()).orElseThrow(() ->
                new ResourceNotFoundException("User not found for email:" + registerRequest.getEmail()));

        exam1.setUser(user);
        examRepository.save(exam1);

        exam1.setPruefungsName("pruefungsname2");
        exam1.setInfo("info2");
        exam1.setBeschreibung("beschreibung2");
        exam1.setErstellDatum("13.08.2023");
        exam1.setAenderungsDatum("13.08.2023");
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
                .andExpect(jsonPath("$.erstellDatum").value("13.08.2023"))
                .andExpect(jsonPath("$.aenderungsDatum").value("13.08.2023"))
                .andExpect(jsonPath("$.anzahlFragen").value(13));

        // Then
        Exam updatedExam = examRepository.findById(exam1.getId()).orElseThrow(() ->
                new ResourceNotFoundException("Updated Exam not found for ID:" + exam1.getId()));
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
                "beschreibung1", "12.08.2023", "12.08.2023", 12);
        User user = userRepository.findByEmail(registerRequest.getEmail()).orElseThrow(() ->
                new ResourceNotFoundException("User not found for email:" + registerRequest.getEmail()));

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