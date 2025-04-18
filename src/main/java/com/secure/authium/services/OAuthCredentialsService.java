package com.secure.authium.services;

import com.secure.authium.configs.OAuthProperties;
import com.secure.authium.dtos.inputs.OAuthProviderCredentials;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OAuthCredentialsService {

    private final OAuthProperties oauthProperties;

    public OAuthCredentialsService(OAuthProperties oauthProperties) {
        this.oauthProperties = oauthProperties;
    }

    public OAuthProviderCredentials getCredentials(String provider) {
        OAuthProperties.ProviderCredentials credentials;

        switch (provider.toLowerCase()) {
            case "github":
                credentials = oauthProperties.getGithub();
                break;
            case "google":
                credentials = oauthProperties.getGoogle();
                break;
            case "facebook":
                credentials = oauthProperties.getFacebook();
                break;
            default:
                throw new IllegalArgumentException("Unsupported provider: " + provider);
        }

        return new OAuthProviderCredentials(
                credentials.getClientId(),
                credentials.getClientSecret(),
                credentials.getRedirectUri()
        );
    }
}
