package com.kopchak.authserver.dto.user;

import com.kopchak.authserver.exception.validation.ValidationStepTwo;
import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@GroupSequence({UserRegistrationDto.class, ValidationStepTwo.class})
public record UserRegistrationDto(
        @NotBlank(message = "Invalid username: username is empty")
        @Pattern(regexp = "^(?=[a-z])[_\\da-z]{2,}$",
                message = "Invalid username: username must start with a lowercase letter, consist of at least " +
                        "3 characters and can only contain lowercase letters, digits, and underscores.",
                groups = ValidationStepTwo.class) String username,


        @NotBlank(message = "Invalid password: password is empty")
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[#$@!%&*?])[A-Za-z\\d#$@!%&*?]{8,16}$",
                message = "Invalid password: password must contain at least 1 number (0-9), 1 uppercase letter, " +
                        "1 lowercase letter, 1 non-alphanumeric number and be 8-16 characters with no space",
                groups = ValidationStepTwo.class) String password) {
}
