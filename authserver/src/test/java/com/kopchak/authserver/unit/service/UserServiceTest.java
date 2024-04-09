package com.kopchak.authserver.unit.service;

import com.kopchak.authserver.domain.AppUser;
import com.kopchak.authserver.domain.Role;
import com.kopchak.authserver.dto.user.UserRegistrationDto;
import com.kopchak.authserver.exception.exception.UsernameAlreadyExistException;
import com.kopchak.authserver.exception.exception.UsernameNotFoundException;
import com.kopchak.authserver.repository.UserRepository;
import com.kopchak.authserver.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserService userService;

    private final static String USERNAME = "username";
    private final static AppUser user = new AppUser();
    private final static UserRegistrationDto userRegistrationDto = new UserRegistrationDto(USERNAME, "password");

    @Test
    public void loadUserByUsername_UsernameExists_UserDetails() {
        when(userRepository.findByUsername(eq(USERNAME))).thenReturn(Optional.of(user));

        UserDetails actualUser = userService.loadUserByUsername(USERNAME);

        assertThat(actualUser).isNotNull();
        assertThat(actualUser).isEqualTo(user);
    }

    @Test
    public void loadUserByUsername_UsernameNotExists_ThrowsUsernameNotFoundException() {
        String exceptionMsg = String.format("User with username: %s is not found!", USERNAME);

        when(userRepository.findByUsername(eq(USERNAME))).thenReturn(Optional.empty());

        assertException(UsernameNotFoundException.class, exceptionMsg, () -> userService.loadUserByUsername(USERNAME));
    }

    @Test
    public void registerUser_UsernameNotExists() {
        String encodedPassword = "encodedPassword";

        when(userRepository.findByUsername(eq(USERNAME))).thenReturn(Optional.empty());
        when(passwordEncoder.encode(eq(userRegistrationDto.password()))).thenReturn(encodedPassword);

        userService.registerUser(userRegistrationDto);

        ArgumentCaptor<AppUser> userCaptor = ArgumentCaptor.forClass(AppUser.class);
        verify(userRepository).save(userCaptor.capture());
        AppUser capturedUser = userCaptor.getValue();

        assertEquals(USERNAME, capturedUser.getUsername());
        assertEquals(encodedPassword, capturedUser.getPassword());
        assertEquals(Role.ROLE_USER, capturedUser.getRole());
    }

    @Test
    public void registerUser_UsernameExists_ThrowsUsernameAlreadyExistException() {
        String exceptionMsg = String.format("The user with the username: %s already exist!", USERNAME);

        when(userRepository.findByUsername(eq(USERNAME))).thenReturn(Optional.of(user));

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