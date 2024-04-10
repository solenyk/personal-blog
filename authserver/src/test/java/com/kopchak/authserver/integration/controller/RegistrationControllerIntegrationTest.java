package com.kopchak.authserver.integration.controller;

import com.kopchak.authserver.dto.error.ErrorInfoDto;
import com.kopchak.authserver.dto.error.MethodArgumentNotValidExceptionDto;
import com.kopchak.authserver.dto.user.UserRegistrationDto;
import com.kopchak.authserver.integration.testcontainer.PostgresContainerBaseTest;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RegistrationControllerIntegrationTest extends PostgresContainerBaseTest {

    @Autowired
    private WebTestClient webTestClient;

    @LocalServerPort
    private int port;
    private static final String VALID_USERNAME = "iryna_kopchak1234";
    private static final UserRegistrationDto validUserRegistrationDto =
            new UserRegistrationDto(VALID_USERNAME, "P@ssword123");
    private String url;

    @BeforeEach
    public void setUrl() {
        url = String.format("http://localhost:%d/api/v1/register", port);
    }

    @Test
    @Order(1)
    @Transactional
    public void registerUser_ReturnsCreatedStatus() {
        webTestClient
                .post().uri("/api/v1/register")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(validUserRegistrationDto)
                .exchange()
                .expectStatus().isCreated();
    }


    @Test
    public void registerUser_MethodArgumentNotValidException_ReturnsBadRequestStatusAndMethodArgumentNotValidExceptionDto() {
        var invalidUserRegistrationDto = new UserRegistrationDto("123", "password");
        var expectedMethodArgNotValidExceptionDto = getMethodArgumentNotValidExceptionDto();

        webTestClient
                .post().uri("/api/v1/register")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidUserRegistrationDto)
                .exchange()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectStatus().isBadRequest()
                .expectBody(MethodArgumentNotValidExceptionDto.class)
                .isEqualTo(expectedMethodArgNotValidExceptionDto);
    }

    @Test
    public void registerUser_UsernameAlreadyExistException_ReturnsConflictStatusAndErrorInfoDto() {
        String errorMsg = String.format("The user with the username: %s already exist!", VALID_USERNAME);
        ErrorInfoDto expectedErrorInfoDto = new ErrorInfoDto(url, errorMsg);

        webTestClient
                .post().uri("/api/v1/register")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(validUserRegistrationDto)
                .exchange()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectStatus().isEqualTo(409)
                .expectBody(ErrorInfoDto.class)
                .isEqualTo(expectedErrorInfoDto);
    }

    @NotNull
    private MethodArgumentNotValidExceptionDto getMethodArgumentNotValidExceptionDto() {
        Map<String, String> fieldsErrorDetails = new LinkedHashMap<>() {{
            put("username", "Invalid username: username must start with a lowercase letter, consist of at least 3 " +
                    "characters and can only contain lowercase letters, digits, and underscores.");
            put("password", "Invalid password: password must contain at least 1 number (0-9), 1 uppercase letter, " +
                    "1 lowercase letter, 1 non-alphanumeric number and be 8-16 characters with no space");
        }};
        return new MethodArgumentNotValidExceptionDto(url, fieldsErrorDetails);
    }
}