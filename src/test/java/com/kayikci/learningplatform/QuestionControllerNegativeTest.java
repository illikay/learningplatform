package com.kayikci.learningplatform;

import com.kayikci.learningplatform.auth.AuthenticationResponse;
import com.kayikci.learningplatform.auth.AuthenticationService;
import com.kayikci.learningplatform.auth.RegisterRequest;
import com.kayikci.learningplatform.domain.Exam;
import com.kayikci.learningplatform.domain.Question;
import com.kayikci.learningplatform.exception.ResourceNotFoundException;
import com.kayikci.learningplatform.repository.ExamRepository;
import com.kayikci.learningplatform.repository.QuestionRepository;
import com.kayikci.learningplatform.token.TokenRepository;
import com.kayikci.learningplatform.user.User;
import com.kayikci.learningplatform.user.UserRepository;
import jakarta.validation.ConstraintViolationException;
import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.AfterEach;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class QuestionControllerNegativeTest {

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
    @Autowired
    private QuestionRepository questionRepository;

    static LocalDateTime dateTime;

    @BeforeAll
    static void beforeAllTests() {
        dateTime = LocalDateTime.of(2023, 9, 7, 13, 45, 30);

    }

    @BeforeEach
    public void setUp() {
        questionRepository.deleteAll();
        examRepository.deleteAll();
        tokenRepository.deleteAll();
        userRepository.deleteAll();


    }

    @AfterEach
    public void afterSetup() {
        questionRepository.deleteAll();
        examRepository.deleteAll();
        tokenRepository.deleteAll();
        userRepository.deleteAll();

    }


    @Test
    public void testCreateQuestionWithInvalidData() {

        RegisterRequest registerRequest = new RegisterRequest("firstname","lastname", "asdf@asdf.de", "Asdf0101!");

        AuthenticationResponse authenticationResponse = authenticationService.register(registerRequest);
        String token = authenticationResponse.getToken();

        Exam exam1 = new Exam("pruefungsname1", "info1",
                "beschreibung1", dateTime, dateTime, 12);
        User user = userRepository.findByEmail(registerRequest.getEmail()).orElseThrow(() ->
                new ResourceNotFoundException("User not found for email:" + registerRequest.getEmail()));

        exam1.setUser(user);
        examRepository.save(exam1);


        Question question1 = new Question("Frage2", "Hinweis1","Lösung1",
                dateTime, dateTime, false);
        question1.setExam(exam1);

        questionRepository.save(question1);

        //Verletzung des Unique-Constraints von questionFrage
        Question invalidQuestion = new Question("Frage2", "Hinweis2","Lösung2",
                dateTime, dateTime, true);

        invalidQuestion.setExam(exam1);


        assertThrows(DataIntegrityViolationException.class, () -> questionRepository.save(invalidQuestion));



    }
}