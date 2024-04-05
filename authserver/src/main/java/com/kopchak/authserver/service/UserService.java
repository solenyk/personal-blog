package com.kopchak.authserver.service;

import com.kopchak.authserver.domain.AppUser;
import com.kopchak.authserver.domain.Role;
import com.kopchak.authserver.dto.user.UserRegistrationDto;
import com.kopchak.authserver.exception.exception.UsernameAlreadyExistException;
import com.kopchak.authserver.exception.exception.UsernameNotFoundException;
import com.kopchak.authserver.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }

    public void registerUser(UserRegistrationDto userRegistrationDto) {
        String email = userRegistrationDto.email();
        if (userRepository.findByUsername(email).isPresent()) {
            throw new UsernameAlreadyExistException(email);
        }
        AppUser user = AppUser.builder()
                .username(email)
                .password(passwordEncoder.encode(userRegistrationDto.password()))
                .role(Role.ROLE_USER)
                .build();
        userRepository.save(user);
    }
}
