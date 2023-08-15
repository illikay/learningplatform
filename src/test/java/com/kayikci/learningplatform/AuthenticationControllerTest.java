package com.kayikci.learningplatform;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kayikci.learningplatform.auth.AuthenticationRequest;
import com.kayikci.learningplatform.auth.AuthenticationResponse;
import com.kayikci.learningplatform.auth.AuthenticationService;
import com.kayikci.learningplatform.auth.RegisterRequest;
import com.kayikci.learningplatform.domain.Exam;
import com.kayikci.learningplatform.exception.ResourceNotFoundException;
import com.kayikci.learningplatform.token.TokenRepository;
import com.kayikci.learningplatform.user.User;
import com.kayikci.learningplatform.user.UserRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.json.JSONException;
import org.json.JSONObject;
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
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenRepository tokenRepository;


    @BeforeEach
    public void setUp() {

        tokenRepository.deleteAll();
        userRepository.deleteAll();
    }

    @AfterEach
    public void afterSetup() {

        tokenRepository.deleteAll();
        userRepository.deleteAll();



    }


    @Test
    public void testRegisterUser() throws  Exception{

        RegisterRequest registerRequest = new RegisterRequest("firstname","lastname", "asdf@asdf.de", "Asdf0101!");

        ResponseEntity<AuthenticationResponse> authenticationResponseEntity = testRestTemplate.postForEntity("/usermanagement/register" , registerRequest, AuthenticationResponse.class);


        assertEquals(HttpStatus.OK, authenticationResponseEntity.getStatusCode());
        AuthenticationResponse authenticationResponse = authenticationResponseEntity.getBody();
        assertThat(authenticationResponse).isNotNull();
        assertThat(authenticationResponse.getToken()).isNotNull();


        User savedUser = userRepository.findByEmail(registerRequest.getEmail()).orElseThrow(() ->
                new ResourceNotFoundException("User not found for E-Mail: " + registerRequest.getEmail()));


        assertThat(savedUser.getFirstname()).isEqualTo(registerRequest.getFirstname());
        assertThat(savedUser.getLastname()).isEqualTo(registerRequest.getLastname());
        assertThat(savedUser.getEmail()).isEqualTo(registerRequest.getEmail());
        assertNotNull(savedUser.getPassword());
        assertNotNull(savedUser.getId());





    }

    @Test
    public void testAuthenticateUser() throws InterruptedException {

        RegisterRequest registerRequest = new RegisterRequest("firstname","lastname", "asdf@asdf.de", "Asdf0101!");


        authenticationService.register(registerRequest);

        AuthenticationRequest authenticationRequest = new AuthenticationRequest(registerRequest.getEmail(), registerRequest.getPassword());

        //Thread.sleep(2000);



        ResponseEntity<AuthenticationResponse> responseEntity = testRestTemplate.postForEntity("/usermanagement/authenticate" ,
                authenticationRequest, AuthenticationResponse.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        AuthenticationResponse authenticationResponse = responseEntity.getBody();
        assertThat(authenticationResponse).isNotNull();
        assertThat(authenticationResponse.getToken()).isNotNull();

    }


    @Test
    public void testRegisterUserWithInvalidData() {

        RegisterRequest registerRequest1 = new RegisterRequest("", "lastname2", "asdf1@jklö.de", "Asdf0101!");
        RegisterRequest registerRequest2 = new RegisterRequest("firstname", "", "asdf2@asdf.de", "Asdf0101!");
        RegisterRequest registerRequest3 = new RegisterRequest("firstname", "lastname", "InvalidEmail", "Asdf0101!");
        RegisterRequest registerRequest4 = new RegisterRequest("firstname", "lastname", "asdf3@jklö.de", "InvalidPassword");

        assertThrows(ConstraintViolationException.class, () -> authenticationService.register(registerRequest1));
        assertThrows(ConstraintViolationException.class, () -> authenticationService.register(registerRequest2));
        assertThrows(ConstraintViolationException.class, () -> authenticationService.register(registerRequest3));

        //tests the behavior with an invalid password
        ResponseEntity<AuthenticationResponse> response = testRestTemplate.postForEntity("/usermanagement/register" ,registerRequest4, AuthenticationResponse.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "HTTP Response status should be a Bad Request");

    }


}
