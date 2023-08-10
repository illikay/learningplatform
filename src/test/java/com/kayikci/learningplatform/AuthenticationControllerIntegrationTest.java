package com.kayikci.learningplatform;

import com.kayikci.learningplatform.auth.AuthenticationResponse;
import com.kayikci.learningplatform.auth.RegisterRequest;
import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

@Disabled("Dauert zu lange")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class AuthenticationControllerIntegrationTest {
    @Autowired
    private DataSource dataSource;
    @Container
    public static MySQLContainer<?> mySQLContainer = new MySQLContainer<>(DockerImageName.parse("mysql:8.0.34"))
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test")
            .waitingFor(Wait.forListeningPort())
            .withEnv("MYSQL_ROOT_HOST", "%");
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;
    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
        registry.add("spring.datasource.password", mySQLContainer::getPassword);
        registry.add("spring.datasource.username", mySQLContainer::getUsername);
    }


    @AfterEach
    void tearDown() {
        if (dataSource instanceof HikariDataSource) {
            ((HikariDataSource) dataSource).close();
        }
    }
    @Test
    public void testRegisterUser() {

        RegisterRequest registerRequest = new RegisterRequest("firstname","lastname", "asdf@asdf.de", "Asdf0101!");

        ResponseEntity<AuthenticationResponse> responseEntity = restTemplate.postForEntity("/usermanagement/register" , registerRequest, AuthenticationResponse.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        AuthenticationResponse authenticationResponse = responseEntity.getBody();
        assertThat(authenticationResponse).isNotNull();
        assertThat(authenticationResponse.getToken()).isNotNull();
    }
}
