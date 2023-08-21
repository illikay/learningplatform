package com.kayikci.learningplatform;

import com.kayikci.learningplatform.auth.AuthenticationResponse;
import com.kayikci.learningplatform.auth.AuthenticationService;
import com.kayikci.learningplatform.auth.RegisterRequest;
import com.kayikci.learningplatform.domain.Question;
import com.kayikci.learningplatform.repository.ExamRepository;
import com.kayikci.learningplatform.repository.QuestionRepository;
import com.kayikci.learningplatform.token.TokenRepository;
import com.kayikci.learningplatform.user.UserRepository;
import jakarta.validation.ConstraintViolationException;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
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


    @Test
    public void testCreateQuestionWithInvalidData() {

        //Ungültiges Datum
        Question invalidQuestion1 = new Question("Frage1", "Hinweis1","Lösung1",
                "InvalidDate", "15.08.2023", false);
        //Ungültiges Datum
        Question invalidQuestion2 = new Question("Frage2", "Hinweis1","Lösung1",
                "15.08.2023", "InvalidDate", false);

        //Verletzung des Unique-Constraints von questionFrage
        Question invalidQuestion3 = new Question("Frage2", "Hinweis1","Lösung1",
                "15.08.2023", "15.08.2023", false);


        assertThrows(ConstraintViolationException.class, () -> questionRepository.save(invalidQuestion1));
        assertThrows(ConstraintViolationException.class, () -> questionRepository.save(invalidQuestion2));
        assertThrows(DataIntegrityViolationException.class, () -> questionRepository.save(invalidQuestion3));



    }
}