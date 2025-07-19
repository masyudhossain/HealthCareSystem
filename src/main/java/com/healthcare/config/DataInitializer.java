package com.healthcare.config;

import com.healthcare.entity.Role;
import com.healthcare.entity.User;
import com.healthcare.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner init(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            createUserIfNotExists(userRepository, passwordEncoder, "admin", "admin123", Role.ADMINISTRATOR);
            createUserIfNotExists(userRepository, passwordEncoder, "doctor", "doctor123", Role.DOCTOR);
            createUserIfNotExists(userRepository, passwordEncoder, "nurse", "nurse123", Role.NURSE);
            createUserIfNotExists(userRepository, passwordEncoder, "patient", "patient123", Role.PATIENT);
        };
    }

    private void createUserIfNotExists(UserRepository repo, PasswordEncoder encoder, String username, String rawPassword, Role role) {
        if (repo.findByUsername(username).isEmpty()) {
            User user = new User();
            user.setUsername(username);
            user.setPassword(encoder.encode(rawPassword));
            user.setRole(role);
            repo.save(user);
            System.out.printf("✅ %s user created: username=%s password=%s%n", role, username, rawPassword);
        }
    }
}
