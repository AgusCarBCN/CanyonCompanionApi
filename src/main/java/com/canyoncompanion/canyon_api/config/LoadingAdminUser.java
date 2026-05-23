package com.canyoncompanion.canyon_api.config;

import com.canyoncompanion.canyon_api.model.entities.RoleEntity;
import com.canyoncompanion.canyon_api.model.entities.UserEntity;
import com.canyoncompanion.canyon_api.model.enums.Roles;
import com.canyoncompanion.canyon_api.model.enums.UserStatus;
import com.canyoncompanion.canyon_api.repository.RoleRepository;
import com.canyoncompanion.canyon_api.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Configuration
@AllArgsConstructor
@Slf4j
public class LoadingAdminUser {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Bean
    public CommandLineRunner loadData() {
        return args -> {

            RoleEntity adminRole = roleRepository.findByRole(Roles.ROLE_ADMIN)
                    .orElseGet(() -> roleRepository.save(
                            RoleEntity.builder()
                                    .role(Roles.ROLE_ADMIN)
                                    .build()
                    ));

            boolean userExists = userRepository.existsByEmailIgnoreCase("agusticar@gmail.com");

            if (!userExists) {

                UserEntity admin = UserEntity.builder()
                        .username("admin")
                        .surname("admin")
                        .email("agusticar@gmail.com")
                        .password(passwordEncoder.encode("Admin123$$"))
                        .status(UserStatus.ACTIVE)
                        .statusDescription("Default admin")
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .roles(Set.of(adminRole))
                        .build();

                userRepository.save(admin);

                log.info("Admin user created");
            } else {
                log.info("Admin user already exists");
            }
        };
    }
}

