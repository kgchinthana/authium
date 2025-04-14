package com.codejam.codex.authzen.repositories;

import com.codejam.codex.authzen.models.Role;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Long> {

    List<Role> findByName(String roleName);

    boolean existsByName(@NotBlank String roleName);
}
