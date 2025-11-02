package com.example.securitydemo.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository, roleRepository);
    }

    @Test
    void synchronizeUserCreatesNewUserWithDefaultRole() {
        User user = userService.synchronizeUser(
            "google-sub-1",
            "first@example.com",
            "First User",
            null,
            null
        );

        assertThat(user.getId()).isNotNull();
        assertThat(user.getRoles())
            .extracting(Role::getName)
            .containsExactly("USER");
    }

    @Test
    void synchronizeUserUpdatesExistingUserAndRoles() {
        userService.synchronizeUser(
            "google-sub-2",
            "initial@example.com",
            "Initial",
            null,
            List.of("USER")
        );

        User updated = userService.synchronizeUser(
            "google-sub-2",
            "updated@example.com",
            "Updated",
            "https://example.com/avatar.png",
            List.of("ADMIN")
        );

        assertThat(updated.getEmail()).isEqualTo("updated@example.com");
        assertThat(updated.getName()).isEqualTo("Updated");
        assertThat(updated.getPicture()).isEqualTo("https://example.com/avatar.png");
        assertThat(updated.getRoles())
            .extracting(Role::getName)
            .containsExactly("ADMIN");
    }

    @Test
    void synchronizeUserDeduplicatesAndNormalizesRoleNames() {
        User user = userService.synchronizeUser(
            "google-sub-3",
            "roles@example.com",
            "Role Tester",
            null,
            List.of("admin", "ADMIN", "user")
        );

        assertThat(user.getRoles())
            .extracting(Role::getName)
            .containsExactlyInAnyOrder("ADMIN", "USER");
    }
}
