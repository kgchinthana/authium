package com.codejam.codex.authzen.repositories;

import com.codejam.codex.authzen.models.OauthProvider;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OauthProviderRepository extends JpaRepository<OauthProvider, Long> {

}
