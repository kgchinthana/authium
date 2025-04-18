package com.secure.authium.services;

import com.secure.authium.dtos.inputs.OAuthProviderCredentials;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class OAuthService {

    private final OAuthCredentialsService oauthCredentialsService;
    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    public OAuthService(OAuthCredentialsService oauthCredentialsService) {
        this.oauthCredentialsService = oauthCredentialsService;
    }

    public String getAccessToken(String code, String provider) {
        OAuthProviderCredentials credentials = oauthCredentialsService.getCredentials(provider);
        String url = getProviderAccessTokenUrl(provider);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> body = new HashMap<>();
        body.put("client_id", credentials.getClientId());
        body.put("client_secret", credentials.getClientSecret());
        body.put("code", code);
        body.put("redirect_uri", credentials.getRedirectUri());

        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return response.getBody().get("access_token").toString();
            } else {
                log.error("Failed to fetch {} access token: {}", provider, response);
                throw new RuntimeException("Failed to get " + provider + " access token");
            }
        } catch (RestClientException e) {
            log.error("Error while fetching {} access token", provider, e);
            throw new RuntimeException(provider + " access token fetch failed", e);
        }
    }

    public Map<String, Object> getUserProfile(String accessToken, String provider) {
        String url = getProviderUserInfoUrl(provider);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        HttpEntity<Void> request = new HttpEntity<>(headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, request, Map.class);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return response.getBody();
            } else {
                log.error("Failed to fetch {} user profile: {}", provider, response);
                throw new RuntimeException("Failed to get " + provider + " user");
            }
        } catch (RestClientException e) {
            log.error("Error while fetching {} user", provider, e);
            throw new RuntimeException(provider + " user fetch failed", e);
        }
    }

    private String getProviderAccessTokenUrl(String provider) {
        switch (provider.toLowerCase()) {
            case "github":
                return "https://github.com/login/oauth/access_token";
            case "google":
                return "https://oauth2.googleapis.com/token";
            case "facebook":
                return "https://graph.facebook.com/v12.0/oauth/access_token";
            default:
                throw new RuntimeException("Unsupported provider: " + provider);
        }
    }

    private String getProviderUserInfoUrl(String provider) {
        switch (provider.toLowerCase()) {
            case "github":
                return "https://api.github.com/user";
            case "google":
                return "https://www.googleapis.com/oauth2/v3/userinfo";
            case "facebook":
                return "https://graph.facebook.com/me?fields=id,name,email";
            default:
                throw new RuntimeException("Unsupported provider: " + provider);
        }
    }
}
