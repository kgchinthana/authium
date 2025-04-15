package com.codejam.codex.authzen.controllers;

import com.codejam.codex.authzen.constants.ApiEndpoint;
import com.codejam.codex.authzen.dtos.inputs.RefreshTokenRequest;
import com.codejam.codex.authzen.dtos.inputs.UpdateUserRequest;
import com.codejam.codex.authzen.dtos.outputs.TokenResponse;
import com.codejam.codex.authzen.dtos.outputs.UserResponse;
import com.codejam.codex.authzen.endpoint.AuthEndpoint;
import com.codejam.codex.authzen.endpoint.UserEndpoint;
import com.codejam.codex.authzen.responses.AuthzenResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Handles secured endpoints related to authenticated user actions such as
 * viewing/updating profile, refreshing tokens, and logout.
 */
@RestController
@RequestMapping(ApiEndpoint.USER)
@PreAuthorize("hasRole('USER')")
public class UserController {

    private final AuthEndpoint authEndpoint;
    private final UserEndpoint userEndpoint;

    @Autowired
    public UserController(AuthEndpoint authEndpoint, UserEndpoint userEndpoint) {
        this.authEndpoint = authEndpoint;
        this.userEndpoint = userEndpoint;
    }

    /**
     * Retrieves the authenticated user's profile.
     *
     * @param request HttpServletRequest with access token
     * @return User profile in standardized response format
     */
    @GetMapping(ApiEndpoint.AUTH_ME)
    public ResponseEntity<AuthzenResponse<UserResponse>> getProfile(HttpServletRequest request) {
        if (!authEndpoint.isAuthenticated(request)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthzenResponse<>(null, false, "Unauthorized: Invalid or missing token"));
        }

        String username = authEndpoint.getUsername(request);
        if (username == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthzenResponse<>(null, false, "Unauthorized: Cannot extract username"));
        }

        UserResponse profile = userEndpoint.getProfile(username);
        return ResponseEntity.ok(new AuthzenResponse<>(profile));
    }

    /**
     * Updates the authenticated user's profile.
     *
     * @param request       HttpServletRequest with access token
     * @param updateRequest Updated user information
     * @return Success message
     */
    @PutMapping(ApiEndpoint.AUTH_UPDATE)
    public ResponseEntity<AuthzenResponse<?>> updateProfile(
            HttpServletRequest request,
            @RequestBody UpdateUserRequest updateRequest
    ) {
        if (!authEndpoint.isAuthenticated(request)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthzenResponse<>(null, false, "Unauthorized"));
        }

        String username = authEndpoint.getUsername(request);
        userEndpoint.updateUser(username, updateRequest);
        return ResponseEntity.ok(new AuthzenResponse<>("User updated successfully"));
    }

    /**
     * Logs out the authenticated user.
     * Note: This is a stateless operation unless token blacklisting is implemented.
     *
     * @param request HttpServletRequest with access token
     * @return Success message
     */
    @PostMapping(ApiEndpoint.AUTH_LOGOUT)
    public ResponseEntity<AuthzenResponse<?>> logout(HttpServletRequest request) {
        if (!authEndpoint.isAuthenticated(request)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthzenResponse<>(null, false, "Unauthorized"));
        }

        boolean blacklisted = authEndpoint.blacklistToken(request);

        if (blacklisted) {
            return ResponseEntity.ok(new AuthzenResponse<>("Logout successful, token blacklisted"));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AuthzenResponse<>(null, false, "Failed to blacklist token"));
        }
    }


    /**
     * Refreshes the access token using a valid refresh token.
     *
     * @param request RefreshTokenRequest with refresh token
     * @return New access and refresh token pair
     */
    @PostMapping(ApiEndpoint.AUTH_REFRESH)
    public ResponseEntity<AuthzenResponse<?>> refreshToken(@RequestBody RefreshTokenRequest request) {
        try {
            TokenResponse tokenResponse = authEndpoint.refreshToken(request.getRefreshToken());
            return ResponseEntity.ok(new AuthzenResponse<>(tokenResponse));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthzenResponse<>(null, false, e.getMessage()));
        }
    }
}