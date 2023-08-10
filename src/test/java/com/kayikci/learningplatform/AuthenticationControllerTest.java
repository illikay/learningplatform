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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class AuthenticationControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private MockMvc mockMvc;

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
    public void testRegisterUser() {

        RegisterRequest registerRequest = new RegisterRequest("firstname","lastname", "asdf@asdf.de", "Asdf0101!");

        ResponseEntity<AuthenticationResponse> responseEntity = testRestTemplate.postForEntity("/usermanagement/register" , registerRequest, AuthenticationResponse.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        AuthenticationResponse authenticationResponse = responseEntity.getBody();
        assertThat(authenticationResponse).isNotNull();
        assertThat(authenticationResponse.getToken()).isNotNull();


    }

    @Test
    public void testAuthenticateUser() throws Exception {

        RegisterRequest registerRequest = new RegisterRequest("firstname2", "lastname2", "asdf2@asdf2.de", "Asdf0101!");
        AuthenticationResponse authenticationResponse = authenticationService.register(registerRequest);
        String token = authenticationResponse.getToken();

        AuthenticationRequest authenticationRequest = new AuthenticationRequest(registerRequest.getEmail(), registerRequest.getPassword());


        mockMvc.perform(post("/usermanagement/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(authenticationRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty());

        // Then
        User savedUser = userRepository.findByEmail(registerRequest.getEmail()).orElseThrow(() ->
                        new ResourceNotFoundException("User not found for E-Mail: " + registerRequest.getEmail()));


        assertThat(savedUser.getFirstname()).isEqualTo(registerRequest.getFirstname());
        assertThat(savedUser.getLastname()).isEqualTo(registerRequest.getLastname());
        assertThat(savedUser.getEmail()).isEqualTo(registerRequest.getEmail());
        assertNotNull(savedUser.getPassword());
        assertNotNull(savedUser.getId());


    }
}
