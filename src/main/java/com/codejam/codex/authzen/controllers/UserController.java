package com.codejam.codex.authzen.controllers;

import com.codejam.codex.authzen.constants.ApiEndpoint;
import com.codejam.codex.authzen.dtos.inputs.RefreshTokenRequest;
import com.codejam.codex.authzen.dtos.inputs.UpdateUserRequest;
import com.codejam.codex.authzen.dtos.outputs.TokenResponse;
import com.codejam.codex.authzen.dtos.outputs.UserResponse;
import com.codejam.codex.authzen.endpoint.UserEndpoint;
import com.codejam.codex.authzen.responses.AuthzenResponse;
import com.codejam.codex.authzen.services.AuthService;
import com.codejam.codex.authzen.utils.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiEndpoint.USER)
//@PreAuthorize("hasRole('USER')")      // commented due to 403 error. need to recheck this
public class UserController {

    private final JwtService jwtService;
    private final AuthService authService;
    private final UserEndpoint userEndpoint;

    @Autowired
    public UserController(JwtService jwtService, AuthService authService, UserEndpoint userEndpoint) {
        this.jwtService = jwtService;
        this.authService = authService;
        this.userEndpoint = userEndpoint;
    }

    @GetMapping(ApiEndpoint.AUTH_ME)
    public ResponseEntity<AuthzenResponse<UserResponse>> getProfile(HttpServletRequest request) {
        if (!authService.isAuthenticated(request)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthzenResponse<>(null, false, "Unauthorized: Invalid or missing token"));
        }

        String username = authService.getUsername(request);
        if (username == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthzenResponse<>(null, false, "Unauthorized: Cannot extract username"));
        }

        UserResponse profile = userEndpoint.getProfile(username);
        return ResponseEntity.ok(new AuthzenResponse<>(profile));
    }

    @PutMapping(ApiEndpoint.AUTH_UPDATE)
    public ResponseEntity<AuthzenResponse<?>> updateProfile(
            HttpServletRequest request,
            @RequestBody UpdateUserRequest updateRequest
    ) {
        if (!authService.isAuthenticated(request)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthzenResponse<>(null, false, "Unauthorized"));
        }

        String username = authService.getUsername(request);
        userEndpoint.updateUser(username, updateRequest);
        return ResponseEntity.ok(new AuthzenResponse<>("User updated successfully"));
    }

    @PostMapping(ApiEndpoint.AUTH_LOGOUT)
    public ResponseEntity<AuthzenResponse<?>> logout(HttpServletRequest request) {
        if (!authService.isAuthenticated(request)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthzenResponse<>(null, false, "Unauthorized"));
        }

        // Optional: Blacklist token or mark session as invalidated
        return ResponseEntity.ok(new AuthzenResponse<>("Logout successful"));
    }

    @PostMapping(ApiEndpoint.AUTH_REFRESH)
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();

        if (!jwtService.isTokenValid(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthzenResponse<>(null, false, "Invalid refresh token"));
        }

        String username = jwtService.extractUsername(refreshToken);
        UserResponse userDetails = authService.getUserDetails(username);

        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthzenResponse<>(null, false, "User not found"));
        }

        String newAccessToken = jwtService.generateAccessToken(userDetails);
        String newRefreshToken = jwtService.generateRefreshToken(userDetails);
        return ResponseEntity.ok(new TokenResponse(newAccessToken, newRefreshToken));
    }
}
