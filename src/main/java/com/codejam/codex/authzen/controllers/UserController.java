package com.codejam.codex.authzen.controllers;


import com.codejam.codex.authzen.constants.ApiEndpoint;
import com.codejam.codex.authzen.dtos.inputs.RefreshTokenRequest;
import com.codejam.codex.authzen.dtos.inputs.UpdateUserRequest;
import com.codejam.codex.authzen.dtos.outputs.TokenResponse;
import com.codejam.codex.authzen.services.UserDetailsService;
import com.codejam.codex.authzen.utils.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiEndpoint.USER)
@PreAuthorize("hasRole('USER')")
public class UserController {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Autowired
    public UserController(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @GetMapping(ApiEndpoint.AUTH_ME)
    public ResponseEntity<?> getProfile() {
        // return user details
        return null;
    }

    @PutMapping(ApiEndpoint.AUTH_UPDATE)
    public ResponseEntity<?> updateProfile(@RequestBody UpdateUserRequest request) {
        // update user info
        return null;
    }

    @PostMapping(ApiEndpoint.AUTH_LOGOUT)
    public ResponseEntity<?> logout(HttpServletRequest request) {
        // invalidate token/session
        return null;
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
}
