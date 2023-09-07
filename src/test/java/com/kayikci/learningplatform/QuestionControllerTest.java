package com.kayikci.learningplatform;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class QuestionControllerTest {

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
    public void testGetQuestionsByExamId() throws Exception {
        // Given

        RegisterRequest registerRequest = new RegisterRequest("firstname","lastname", "asdf@asdf.de", "Asdf0101!");

        AuthenticationResponse authenticationResponse = authenticationService.register(registerRequest);
        String token = authenticationResponse.getToken();

        Exam exam1 = new Exam("pruefungsname1", "info1",
                "beschreibung1", dateTime, dateTime, 12);
        User user = userRepository.findByEmail(registerRequest.getEmail()).orElseThrow(() ->
               new ResourceNotFoundException("User not found for email:" + registerRequest.getEmail()));

        exam1.setUser(user);
        examRepository.save(exam1);

        Question question = new Question("Frage1", "Hinweis1","Lösung1",
                dateTime, dateTime, false);

        question.setExam(exam1);
        questionRepository.save(question);




        // When
        mockMvc.perform(get("/exam/{examId}/questions", exam1.getId())
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].questionFrage").value("Frage1"))
                .andExpect(jsonPath("$[0].questionHinweis").value("Hinweis1"))
                .andExpect(jsonPath("$[0].questionLoesung").value("Lösung1"))
                .andExpect(jsonPath("$[0].erstellDatum").value(dateTime.toString()))
                .andExpect(jsonPath("$[0].aenderungsDatum").value(dateTime.toString()))
                .andExpect(jsonPath("$[0].beantwortet").value(false));
    }

    @Test
    public void testGetQuestionById() throws Exception {

        // Given

        RegisterRequest registerRequest = new RegisterRequest("firstname","lastname", "asdf@asdf.de", "Asdf0101!");

        AuthenticationResponse authenticationResponse = authenticationService.register(registerRequest);
        String token = authenticationResponse.getToken();

        Exam exam1 = new Exam("pruefungsname1", "info1",
                "beschreibung1", dateTime, dateTime, 12);
        User user = userRepository.findByEmail(registerRequest.getEmail()).orElseThrow(() ->
                new ResourceNotFoundException("User not found for email:" + registerRequest.getEmail()));

        exam1.setUser(user);
        examRepository.save(exam1);

        Question question1 = new Question("Frage1", "Hinweis1","Lösung1",
                dateTime, dateTime, false);

        question1.setExam(exam1);
        questionRepository.save(question1);



        // When
        mockMvc.perform(get("/exam/{examId}/questions/{questionId}", exam1.getId(), question1.getId())
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.questionFrage").value("Frage1"))
                .andExpect(jsonPath("$.questionHinweis").value("Hinweis1"))
                .andExpect(jsonPath("$.questionLoesung").value("Lösung1"))
                .andExpect(jsonPath("$.erstellDatum").value(dateTime.toString()))
                .andExpect(jsonPath("$.aenderungsDatum").value(dateTime.toString()))
                .andExpect(jsonPath("$.beantwortet").value(false));
    }

    @Test
    public void testCreateQuestion() throws Exception {
        // Given

        RegisterRequest registerRequest = new RegisterRequest("firstname","lastname", "asdf@asdf.de", "Asdf0101!");

        AuthenticationResponse authenticationResponse = authenticationService.register(registerRequest);
        String token = authenticationResponse.getToken();

        Exam exam1 = new Exam("pruefungsname1", "info1",
                "beschreibung1", dateTime, dateTime, 12);
        User user = userRepository.findByEmail(registerRequest.getEmail()).orElseThrow(() ->
                new ResourceNotFoundException("User not found for email:" + registerRequest.getEmail()));

        exam1.setUser(user);
        examRepository.save(exam1);

        Question question1 = new Question("Frage1", "Hinweis1","Lösung1",
                dateTime, dateTime, false);

        question1.setExam(exam1);
        //questionRepository.save(question1);


        // When
        mockMvc.perform(post("/exam/{examId}/questions", exam1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(question1))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.questionFrage").value("Frage1"))
                .andExpect(jsonPath("$.questionHinweis").value("Hinweis1"))
                .andExpect(jsonPath("$.questionLoesung").value("Lösung1"))
                .andExpect(jsonPath("$.erstellDatum").value(dateTime.toString()))
                .andExpect(jsonPath("$.aenderungsDatum").value(dateTime.toString()))
                .andExpect(jsonPath("$.beantwortet").value(false));
        //Todo Question-Controller-Test ausfüllen
        // Then
        Question savedQuestion = questionRepository.findByQuestionFrage(question1.getQuestionFrage()).orElseThrow(() ->
                new ResourceNotFoundException("Question not found for questionFrage: " + question1.getQuestionFrage()));

        assertThat(savedQuestion.getQuestionFrage()).isEqualTo(question1.getQuestionFrage());
        assertThat(savedQuestion.getQuestionHinweis()).isEqualTo(question1.getQuestionHinweis());
        assertThat(savedQuestion.getQuestionLoesung()).isEqualTo(question1.getQuestionLoesung());
        assertThat(savedQuestion.getErstellDatum()).isEqualTo(question1.getErstellDatum());
        assertThat(savedQuestion.getAenderungsDatum()).isEqualTo(question1.getAenderungsDatum());
        assertThat(savedQuestion.isBeantwortet()).isEqualTo(question1.isBeantwortet());
    }

    @Test
    public void testUpdateQuestion() throws Exception {
        // Given

        RegisterRequest registerRequest = new RegisterRequest("firstname","lastname", "asdf@asdf.de", "Asdf0101!");

        AuthenticationResponse authenticationResponse = authenticationService.register(registerRequest);
        String token = authenticationResponse.getToken();

        Exam exam1 = new Exam("pruefungsname1", "info1",
                "beschreibung1", dateTime, dateTime, 12);
        User user = userRepository.findByEmail(registerRequest.getEmail()).orElseThrow(() ->
                new ResourceNotFoundException("User not found for email:" + registerRequest.getEmail()));

        exam1.setUser(user);
        examRepository.save(exam1);

        Question question1 = new Question("Frage1", "Hinweis1","Lösung1",
                dateTime, dateTime, false);

        question1.setExam(exam1);
        questionRepository.save(question1);

        question1.setQuestionFrage("Frage2");
        question1.setQuestionHinweis("Hinweis2");
        question1.setQuestionLoesung("Lösung2");
        question1.setErstellDatum(dateTime);
        question1.setAenderungsDatum(dateTime);
        question1.setBeantwortet(true);


        // When
        mockMvc.perform(put("/exam/{examId}/questions/{questionId}", exam1.getId(), question1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(question1))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.questionFrage").value("Frage2"))
                .andExpect(jsonPath("$.questionHinweis").value("Hinweis2"))
                .andExpect(jsonPath("$.questionLoesung").value("Lösung2"))
                .andExpect(jsonPath("$.erstellDatum").value(dateTime.toString()))
                .andExpect(jsonPath("$.aenderungsDatum").value(dateTime.toString()))
                .andExpect(jsonPath("$.beantwortet").value(true));

        // Then
        Question updatedQuestion = questionRepository.findByQuestionFrage(question1.getQuestionFrage()).orElseThrow(() ->
                new ResourceNotFoundException("Question not found for questionFrage: " + question1.getQuestionFrage()));

        assertThat(updatedQuestion.getQuestionFrage()).isEqualTo(question1.getQuestionFrage());
        assertThat(updatedQuestion.getQuestionHinweis()).isEqualTo(question1.getQuestionHinweis());
        assertThat(updatedQuestion.getQuestionLoesung()).isEqualTo(question1.getQuestionLoesung());
        assertThat(updatedQuestion.getErstellDatum()).isEqualTo(question1.getErstellDatum());
        assertThat(updatedQuestion.getAenderungsDatum()).isEqualTo(question1.getAenderungsDatum());
        assertThat(updatedQuestion.isBeantwortet()).isEqualTo(question1.isBeantwortet());
    }

    @Test
    public void testDeleteQuestion() throws Exception {
        // Given

        RegisterRequest registerRequest = new RegisterRequest("firstname","lastname", "asdf@asdf.de", "Asdf0101!");

        AuthenticationResponse authenticationResponse = authenticationService.register(registerRequest);
        String token = authenticationResponse.getToken();

        Exam exam1 = new Exam("pruefungsname1", "info1",
                "beschreibung1", dateTime, dateTime, 12);
        User user = userRepository.findByEmail(registerRequest.getEmail()).orElseThrow(() ->
                new ResourceNotFoundException("User not found for email:" + registerRequest.getEmail()));

        exam1.setUser(user);
        examRepository.save(exam1);

        Question question1 = new Question("Frage1", "Hinweis1","Lösung1",
                dateTime, dateTime, false);

        question1.setExam(exam1);
        questionRepository.save(question1);

        // When
        mockMvc.perform(delete("/exam/{examId}/questions/{questionId}", exam1.getId(), question1.getId())
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());

        // Then
        assertThat(questionRepository.findById(question1.getId())).isEmpty();
    }



}
