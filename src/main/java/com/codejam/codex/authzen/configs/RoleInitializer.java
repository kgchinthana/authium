package com.codejam.codex.authzen.configs;

import com.codejam.codex.authzen.models.Role;
import com.codejam.codex.authzen.repositories.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RoleInitializer {

    @Bean
    CommandLineRunner initializeRoles(RoleRepository roleRepository) {
        return args -> {
            createRoleIfNotExists(roleRepository, "ROLE_USER", "Default user role");
            createRoleIfNotExists(roleRepository, "ROLE_ADMIN", "Administrator with full access");
        };
    }

    private void createRoleIfNotExists(RoleRepository roleRepository, String name, String description) {
        if (roleRepository.findByName(name).isEmpty()) {
            Role role = Role.builder()
                    .name(name)
                    .description(description)
                    .build();
            roleRepository.save(role);
        }
    }

}