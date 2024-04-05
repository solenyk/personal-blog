package com.kopchak.authserver.dto.user;

import com.kopchak.authserver.exception.validation.ValidationStepTwo;
import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@GroupSequence({UserRegistrationDto.class, ValidationStepTwo.class})
public record UserRegistrationDto(
        @NotBlank(message = "Invalid email: email is empty")
        @Email(regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+" +
                "(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$",
                message = "Invalid email: email '${validatedValue}' format is incorrect",
                groups = ValidationStepTwo.class) String email,


        @NotBlank(message = "Invalid password: password is empty")
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[#$@!%&*?])[A-Za-z\\d#$@!%&*?]{8,16}$",
                message = "Invalid password: password must contain at least 1 number (0-9), 1 uppercase letter, " +
                        "1 lowercase letter, 1 non-alphanumeric number and be 8-16 characters with no space",
                groups = ValidationStepTwo.class) String password) {
}
