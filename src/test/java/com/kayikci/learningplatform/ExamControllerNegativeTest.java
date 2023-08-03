package com.kayikci.learningplatform;

import com.kayikci.learningplatform.auth.AuthenticationResponse;
import com.kayikci.learningplatform.auth.AuthenticationService;
import com.kayikci.learningplatform.auth.RegisterRequest;
import com.kayikci.learningplatform.domain.Exam;
import com.kayikci.learningplatform.exception.ResourceNotFoundException;
import com.kayikci.learningplatform.repository.ExamRepository;
import com.kayikci.learningplatform.token.TokenRepository;
import com.kayikci.learningplatform.user.User;
import com.kayikci.learningplatform.user.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;



import org.springframework.http.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ExamControllerNegativeTest {

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

    @LocalServerPort
    private int port;

    @BeforeAll
    static void beforeAllTests() {

    }

    @BeforeEach
    public void setUp() {
        examRepository.deleteAll();
        tokenRepository.deleteAll();
        userRepository.deleteAll();


    }

    @Test
    public void testCreateExamWithInvalidData() throws Exception {
        // Given
        RegisterRequest registerRequest = new RegisterRequest("firstname", "lastname", "asdf@asdf.de", "Asdf0101!");
        AuthenticationResponse authenticationResponse = authenticationService.register(registerRequest);
        String token = authenticationResponse.getToken();

        Exam invalidExam = new Exam(null , "prüfungsinfo1", // Ungültige Werte hier
                "beschreibung1", "erstelldatum1", "aenderungsdatum1", 12);

        Exam invalidExam2 = new Exam("prüfungsname1" , "prüfungsinfo1",
                "beschreibung1", "erstelldatum1", "aenderungsdatum1", -4); // Negative Anzahl von Fragen

        // When
        mockMvc.perform(post("/exam")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(invalidExam))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isBadRequest()); // Erwarteter Statuscode für ungültige Daten

        mockMvc.perform(post("/exam")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(invalidExam2))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isBadRequest()); // Erwarteter Statuscode für ungültige Daten
    }

    @Test
    public void testAccessExamWithInvalidIds() throws Exception {

        RegisterRequest registerRequest = new RegisterRequest("firstname","lastname", "asdf@asdf.de", "Asdf0101!");
        AuthenticationResponse authenticationResponse = authenticationService.register(registerRequest);
        String token = authenticationResponse.getToken();

        Exam exam1 = new Exam("pruefungsname1", "info1",
                "beschreibung1", "erstelldatum1", "aenderungsdatum1", 12);
        User user = userRepository.findByEmail(registerRequest.getEmail()).orElseThrow(() ->
                new ResourceNotFoundException("User not found for email:" + registerRequest.getEmail()));

        exam1.setUser(user);
        examRepository.save(exam1);

        Optional<Exam> result = examRepository.findByIdAndUserId(1L, -1L); //Invalid User-Id
        assertFalse(result.isPresent());

        Optional<Exam> result2 = examRepository.findByIdAndUserId(-1L, user.getId()); //Invalid Exam-Id
        assertFalse(result2.isPresent());

    }


}
