package com.scms.config;

import com.scms.entity.Role;
import com.scms.entity.User;
import com.scms.enums.UserRole;
import com.scms.repository.RoleRepository;
import com.scms.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        seedRoles();
        seedDefaultUsers();
    }

    private void seedRoles() {
        createRoleIfMissing(UserRole.ROLE_ADMIN, "System administrator with full access");
        createRoleIfMissing(UserRole.ROLE_USER, "Regular user who can submit complaints");
        createRoleIfMissing(UserRole.ROLE_STAFF, "Staff member who handles complaints");
    }

    private void createRoleIfMissing(UserRole roleName, String description) {
        if (!roleRepository.existsByName(roleName)) {
            roleRepository.save(Role.builder()
                    .name(roleName)
                    .description(description)
                    .build());
            log.info("Seeded role: {}", roleName);
        }
    }

    private void seedDefaultUsers() {
        seedUser("admin@scms.com", "System", "Admin", "9876543210", UserRole.ROLE_ADMIN);
        seedUser("user@scms.com", "John", "Doe", "9876543212", UserRole.ROLE_USER);
    }

    private void seedUser(String email, String firstName, String lastName, String phone, UserRole roleName) {
        if (userRepository.existsByEmail(email)) {
            return;
        }

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new IllegalStateException("Role not found: " + roleName));

        User user = User.builder()
                .email(email)
                .password(passwordEncoder.encode("password"))
                .firstName(firstName)
                .lastName(lastName)
                .phone(phone)
                .active(true)
                .roles(Set.of(role))
                .build();

        userRepository.save(user);
        log.info("Seeded default user: {} with role {}", email, roleName);
    }
}
