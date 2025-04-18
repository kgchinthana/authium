package com.secure.authium.configs;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "oauth")
public class OAuthProperties {

    private ProviderCredentials github;
    private ProviderCredentials google;
    private ProviderCredentials facebook;

    @Getter
    @Setter
    public static class ProviderCredentials {
        private String clientId;
        private String clientSecret;
        private String redirectUri;
    }
}
