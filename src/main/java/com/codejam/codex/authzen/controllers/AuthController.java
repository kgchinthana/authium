package com.codejam.codex.authzen.controllers;

import com.codejam.codex.authzen.constants.ApiEndpoint;
import com.codejam.codex.authzen.dtos.inputs.*;
import com.codejam.codex.authzen.dtos.outputs.TokenResponse;
import com.codejam.codex.authzen.services.UserDetailsService;
import com.codejam.codex.authzen.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiEndpoint.AUTH)
public class AuthController {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Autowired
    public AuthController(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping(ApiEndpoint.AUTH_REFRESH)
    public ResponseEntity<TokenResponse> refreshToken(
            @RequestBody RefreshTokenRequest request
    ) {
        String refreshToken = request.getRefreshToken();

        if (!jwtService.isTokenValid(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String username = jwtService.extractUsername(refreshToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        String newAccessToken = jwtService.generateAccessToken(userDetails);

        return ResponseEntity.ok(new TokenResponse(newAccessToken));
    }

    @PostMapping(ApiEndpoint.AUTH_REGISTER)
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        // register user
        return null;
    }

    @PostMapping(ApiEndpoint.AUTH_LOGIN)
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        // authenticate user
        return null;
    }

    @PostMapping(ApiEndpoint.AUTH_OAUTH)
    public ResponseEntity<?> oauthLogin(@RequestBody OAuthRequest request) {
        // handle OAuth login
        return null;
    }

    @PostMapping(ApiEndpoint.AUTH_RESET_REQUEST)
    public ResponseEntity<?> resetPasswordRequest(@RequestBody ResetRequest request) {
        // send reset email/token
        return null;
    }

    @PostMapping(ApiEndpoint.AUTH_RESET_PASSWORD)
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
        // reset password
        return null;
    }
}
