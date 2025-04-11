package com.codejam.codex.authzen.repositories;

import com.codejam.codex.authzen.models.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission, Long> {

}
