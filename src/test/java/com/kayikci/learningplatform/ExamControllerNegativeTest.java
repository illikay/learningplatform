package com.kayikci.learningplatform;

import com.kayikci.learningplatform.auth.AuthenticationResponse;
import com.kayikci.learningplatform.auth.AuthenticationService;
import com.kayikci.learningplatform.auth.RegisterRequest;
import com.kayikci.learningplatform.domain.Exam;
import com.kayikci.learningplatform.domain.Question;
import com.kayikci.learningplatform.exception.ResourceNotFoundException;
import com.kayikci.learningplatform.repository.ExamRepository;
import com.kayikci.learningplatform.token.TokenRepository;
import com.kayikci.learningplatform.user.User;
import com.kayikci.learningplatform.user.UserRepository;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;



import org.springframework.http.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;




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
    
    static ZonedDateTime dateTime;

    @BeforeAll
    static void beforeAllTests() {
        dateTime = ZonedDateTime.of(2023, 9, 10, 12, 12, 12, 1234, ZoneOffset.UTC);


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

        //pruefungsname darf nicht null sein
        Exam invalidExam = new Exam(null , "prüfungsinfo1",
                "beschreibung1", dateTime, dateTime, 12);
        //anzahl Fragen darf nicht negativ sein
        Exam invalidExam2 = new Exam("prüfungsname1" , "prüfungsinfo1",
                "beschreibung1", dateTime, dateTime, -4);

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
                "beschreibung1", dateTime, dateTime, 12);
        User user = userRepository.findByEmail(registerRequest.getEmail()).orElseThrow(() ->
                new ResourceNotFoundException("User not found for email:" + registerRequest.getEmail()));

        exam1.setUser(user);
        examRepository.save(exam1);

        Optional<Exam> result = examRepository.findByIdAndUserId(1L, -1L); //Invalid User-Id
        assertFalse(result.isPresent());

        Optional<Exam> result2 = examRepository.findByIdAndUserId(-1L, user.getId()); //Invalid Exam-Id
        assertFalse(result2.isPresent());

    }

    @Test
    public void testSaveExamWithInvalidData() {

        RegisterRequest registerRequest = new RegisterRequest("firstname","lastname", "asdf@asdf.de", "Asdf0101!");
        AuthenticationResponse authenticationResponse = authenticationService.register(registerRequest);
        String token = authenticationResponse.getToken();

        Exam exam1 = new Exam("pruefungsname1", "info1",
                "beschreibung1", dateTime, dateTime, 12);
        User user = userRepository.findByEmail(registerRequest.getEmail()).orElseThrow(() ->
                new ResourceNotFoundException("User not found for email:" + registerRequest.getEmail()));

        exam1.setUser(user);
        examRepository.save(exam1);



        //Verletzung des Unique-Constraints von pruefungsName, pruefungsname sollte nicht doppelt vorhanden sein
        Exam invalidExam2 = new Exam("pruefungsname1" , "prüfungsinfo2",
                "beschreibung2", dateTime, dateTime, 15);
        invalidExam2.setUser(user);


        assertThrows(DataIntegrityViolationException.class, () -> examRepository.save(invalidExam2));




    }


}
