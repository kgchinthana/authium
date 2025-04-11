package com.codejam.codex.authzen.repositories;

import com.codejam.codex.authzen.models.EmailToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailTokenRepository extends JpaRepository<EmailToken, Long> {
}
