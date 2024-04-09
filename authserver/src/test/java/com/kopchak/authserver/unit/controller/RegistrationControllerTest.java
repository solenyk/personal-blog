package com.kopchak.authserver.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kopchak.authserver.controller.RegistrationController;
import com.kopchak.authserver.dto.error.ErrorInfoDto;
import com.kopchak.authserver.dto.error.MethodArgumentNotValidExceptionDto;
import com.kopchak.authserver.dto.user.UserRegistrationDto;
import com.kopchak.authserver.exception.exception.UsernameNotFoundException;
import com.kopchak.authserver.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(controllers = RegistrationController.class)
@AutoConfigureMockMvc(addFilters = false)
class RegistrationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String VALID_EMAIL = "user@gmail.com";
    private static final String URL = "http://localhost/api/v1/register";
    private static final UserRegistrationDto validUserRegistrationDto =
            new UserRegistrationDto(VALID_EMAIL, "P@ssword123");

    @Test
    public void registerUser_ReturnsCreatedStatus() throws Exception {
        doNothing().when(userService).registerUser(eq(validUserRegistrationDto));

        ResultActions response = mockMvc.perform(post("/api/v1/register")
                .content(objectMapper.writeValueAsString(validUserRegistrationDto))
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void registerUser_MethodArgumentNotValidException_ReturnsBadRequestStatusAndMethodArgumentNotValidExceptionDto() throws Exception {
        var invalidUserRegistrationDto = new UserRegistrationDto("username", "password");
        Map<String, String> fieldsErrorDetails = new LinkedHashMap<>() {{
            put("password", "Invalid password: password must contain at least 1 number (0-9), 1 uppercase letter, " +
                    "1 lowercase letter, 1 non-alphanumeric number and be 8-16 characters with no space");
            put("email", "Invalid email: email 'username' format is incorrect");
        }};
        var expectedMethodArgNotValidExceptionDto = new MethodArgumentNotValidExceptionDto(URL, fieldsErrorDetails);


        doNothing().when(userService).registerUser(eq(invalidUserRegistrationDto));

        ResultActions response = mockMvc.perform(post("/api/v1/register")
                .content(objectMapper.writeValueAsString(invalidUserRegistrationDto))
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedMethodArgNotValidExceptionDto)))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void registerUser_UsernameNotFoundException_ReturnsNotFoundStatusAndErrorInfoDto() throws Exception {
        String errorMsg = String.format("User with username: %s is not found!", VALID_EMAIL);
        ErrorInfoDto expectedErrorInfoDto = new ErrorInfoDto(URL, errorMsg);

        doThrow(new UsernameNotFoundException(VALID_EMAIL)).when(userService).registerUser(eq(validUserRegistrationDto));

        ResultActions response = mockMvc.perform(post("/api/v1/register")
                .content(objectMapper.writeValueAsString(validUserRegistrationDto))
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedErrorInfoDto)))
                .andDo(MockMvcResultHandlers.print());
    }
}