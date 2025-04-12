package com.codejam.codex.authzen.controllers;

import com.codejam.codex.authzen.constants.ApiEndpoint;
import com.codejam.codex.authzen.dtos.inputs.*;
import com.codejam.codex.authzen.dtos.outputs.TokenResponse;
import com.codejam.codex.authzen.responses.AuthzenResponse;
import com.codejam.codex.authzen.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiEndpoint.AUTH)
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Registers a new user.
     *
     * @param request The registration request containing user details.
     * @return A ResponseEntity with the result of the registration.
     */
    @PostMapping(ApiEndpoint.AUTH_REGISTER)
    public ResponseEntity<AuthzenResponse<?>> register(@RequestBody RegisterRequest request) {
        try {
            boolean isRegistered = authService.registerUser(request);
            if (isRegistered) {
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(new AuthzenResponse<>("User registered successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new AuthzenResponse<>(null, false, "Registration failed"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AuthzenResponse<>(null, false, "An error occurred during registration"));
        }
    }

    /**
     * Authenticates a user and issues a token.
     *
     * @param request The login request containing user credentials.
     * @return A ResponseEntity with the result of the login process.
     */
    @PostMapping(ApiEndpoint.AUTH_LOGIN)
    public ResponseEntity<AuthzenResponse<?>> login(@RequestBody LoginRequest request) {
        try {
            String token = authService.authenticateUser(request);
            if (token != null) {
                return ResponseEntity.ok(new AuthzenResponse<>(new TokenResponse(token)));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new AuthzenResponse<>(null, false, "Invalid credentials"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AuthzenResponse<>(null, false, "An error occurred during login"));
        }
    }

    /**
     * Handles OAuth login.
     *
     * @param request The OAuth login request containing OAuth credentials.
     * @return A ResponseEntity with the result of the OAuth login.
     */
    @PostMapping(ApiEndpoint.AUTH_OAUTH)
    public ResponseEntity<AuthzenResponse<?>> oauthLogin(@RequestBody OAuthRequest request) {
        try {
            String oauthToken = authService.authenticateOAuth(request);
            if (oauthToken != null) {
                return ResponseEntity.ok(new AuthzenResponse<>(new TokenResponse(oauthToken)));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new AuthzenResponse<>(null, false, "OAuth login failed"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AuthzenResponse<>(null, false, "An error occurred during OAuth login"));
        }
    }

    /**
     * Requests a password reset by sending an email or token.
     *
     * @param request The reset request containing the user's email.
     * @return A ResponseEntity with the result of the reset request.
     */
    @PostMapping(ApiEndpoint.AUTH_RESET_REQUEST)
    public ResponseEntity<AuthzenResponse<?>> resetPasswordRequest(@RequestBody ResetRequest request) {
        try {
            boolean emailSent = authService.sendPasswordResetEmail(request);
            if (emailSent) {
                return ResponseEntity.ok(new AuthzenResponse<>("Password reset email sent"));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new AuthzenResponse<>(null, false, "Failed to send reset email"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AuthzenResponse<>(null, false, "An error occurred while sending reset request"));
        }
    }

    /**
     * Resets the user's password using the provided token.
     *
     * @param request The reset password request containing token and new password.
     * @return A ResponseEntity with the result of the password reset.
     */
    @PostMapping(ApiEndpoint.AUTH_RESET_PASSWORD)
    public ResponseEntity<AuthzenResponse<?>> resetPassword(@RequestBody ResetPasswordRequest request) {
        try {
            boolean isPasswordReset = authService.resetUserPassword(request);
            if (isPasswordReset) {
                return ResponseEntity.ok(new AuthzenResponse<>("Password reset successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new AuthzenResponse<>(null, false, "Failed to reset password"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AuthzenResponse<>(null, false, "An error occurred during password reset"));
        }
    }
}
