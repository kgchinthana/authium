package com.secure.authium.dtos.inputs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OAuthProviderCredentials {
    private String clientId;
    private String clientSecret;
    private String redirectUri;
}
