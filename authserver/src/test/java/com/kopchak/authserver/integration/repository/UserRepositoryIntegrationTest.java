package com.kopchak.authserver.integration.repository;

import com.kopchak.authserver.domain.AppUser;
import com.kopchak.authserver.domain.Role;
import com.kopchak.authserver.integration.testcontainer.PostgresContainerBaseTest;
import com.kopchak.authserver.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryIntegrationTest extends PostgresContainerBaseTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    public void findByUsername_UsernameExists_OptionalOfAppUser() {
        String username = "user";
        AppUser expectedUser = AppUser
                .builder()
                .id(2)
                .username(username)
                .role(Role.ROLE_USER)
                .build();

        Optional<AppUser> actualOptionalUser = userRepository.findByUsername(username);

        assertThat(actualOptionalUser).isNotNull();
        assertThat(actualOptionalUser).isNotEmpty();

        assertThat(actualOptionalUser.get()).usingRecursiveComparison()
                .ignoringFields("password").isEqualTo(expectedUser);
    }
}