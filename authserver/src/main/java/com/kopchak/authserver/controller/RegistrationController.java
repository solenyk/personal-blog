package com.kopchak.authserver.controller;

import com.kopchak.authserver.dto.error.ErrorInfoDto;
import com.kopchak.authserver.dto.error.MethodArgumentNotValidExceptionDto;
import com.kopchak.authserver.dto.user.UserRegistrationDto;
import com.kopchak.authserver.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/register")
@CrossOrigin
@RequiredArgsConstructor
@Tag(name = "registration-controller", description = "The registration controller is responsible for user registration.")
public class RegistrationController {
    private final UserService userService;

    @Operation(summary = "User registration")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "The user was successfully registered",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(
                    responseCode = "400",
                    description = "User registration data is invalid",
                    content = @Content(schema = @Schema(implementation = MethodArgumentNotValidExceptionDto.class))),
            @ApiResponse(
                    responseCode = "409",
                    description = "User with this username already exists",
                    content = @Content(schema = @Schema(implementation = ErrorInfoDto.class)))
    })
    @PostMapping
    public ResponseEntity<Void> registerUser(@Valid @RequestBody UserRegistrationDto userRegistrationDto) {
        userService.registerUser(userRegistrationDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
