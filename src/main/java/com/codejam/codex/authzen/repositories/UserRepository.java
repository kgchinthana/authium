package com.codejam.codex.authzen.repositories;

import com.codejam.codex.authzen.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
