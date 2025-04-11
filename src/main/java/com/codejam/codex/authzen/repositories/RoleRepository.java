package com.codejam.codex.authzen.repositories;

import com.codejam.codex.authzen.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

}
