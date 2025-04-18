package com.secure.authium.repositories;

import com.secure.authium.models.OauthProvider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OauthProviderRepository extends JpaRepository<OauthProvider, Long> {

    Optional<OauthProvider> findByProviderAndExternalUserId(String github, String githubId);
}
