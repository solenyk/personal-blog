package com.kopchak.authserver.integration.service;

import com.kopchak.authserver.domain.AppUser;
import com.kopchak.authserver.domain.Role;
import com.kopchak.authserver.dto.user.UserRegistrationDto;
import com.kopchak.authserver.exception.exception.UsernameAlreadyExistException;
import com.kopchak.authserver.exception.exception.UsernameNotFoundException;
import com.kopchak.authserver.integration.testcontainer.PostgresContainerBaseTest;
import com.kopchak.authserver.repository.UserRepository;
import com.kopchak.authserver.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserServiceIntegrationTest extends PostgresContainerBaseTest {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    private final static String EXISTENT_USERNAME = "user";
    private final static String NON_EXISTENT_USERNAME = "non_existent_username";

    @Test
    public void loadUserByUsername_UsernameExists_UserDetails() {
        AppUser expectedUser = userRepository.findByUsername(EXISTENT_USERNAME).orElseThrow();

        AppUser actualUser = (AppUser) userService.loadUserByUsername(EXISTENT_USERNAME);

        assertThat(actualUser).isNotNull();
        assertThat(actualUser).usingRecursiveComparison()
                .isEqualTo(expectedUser);
    }

    @Test
    public void loadUserByUsername_UsernameNotExists_ThrowsUsernameNotFoundException() {
        String exceptionMsg = String.format("User with username: %s is not found!", NON_EXISTENT_USERNAME);

        assertException(UsernameNotFoundException.class, exceptionMsg,
                () -> userService.loadUserByUsername(NON_EXISTENT_USERNAME));
    }

    @Test
    @Transactional
    public void registerUser_UsernameNotExists() {
        var userRegistrationDto = new UserRegistrationDto(NON_EXISTENT_USERNAME, "password");
        AppUser expectedUser = AppUser
                .builder()
                .username(NON_EXISTENT_USERNAME)
                .role(Role.ROLE_USER)
                .build();

        userService.registerUser(userRegistrationDto);

        Optional<AppUser> savedUser = userRepository.findByUsername(NON_EXISTENT_USERNAME);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser).isPresent();
        assertThat(savedUser.orElseThrow()).usingRecursiveComparison()
                .ignoringFields("id", "password")
                .isEqualTo(expectedUser);
    }

    @Test
    public void registerUser_UsernameExists_ThrowsUsernameAlreadyExistException() {
        var userRegistrationDto = new UserRegistrationDto(EXISTENT_USERNAME, "password");
        String exceptionMsg = String.format("The user with the username: %s already exist!", EXISTENT_USERNAME);

        assertException(UsernameAlreadyExistException.class, exceptionMsg,
                () -> userService.registerUser(userRegistrationDto));
    }

    private void assertException(Class<? extends Exception> expectedExceptionType, String expectedMessage,
                                 Executable executable) {
        Exception exception = assertThrows(expectedExceptionType, executable);
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }
}