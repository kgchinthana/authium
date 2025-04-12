package com.codejam.codex.authzen.services;

import com.codejam.codex.authzen.dtos.inputs.*;
import com.codejam.codex.authzen.models.User;
import com.codejam.codex.authzen.repositories.UserRepository;
import com.codejam.codex.authzen.utils.EmailUtil;
import com.codejam.codex.authzen.utils.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class AuthService {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final EmailUtil emailUtil; // Inject EmailUtils

    @Autowired
    public AuthService(JwtService jwtService, UserDetailsService userDetailsService, UserRepository userRepository,
                       BCryptPasswordEncoder passwordEncoder, EmailUtil emailUtil) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailUtil = emailUtil; // Injected
    }

    /**
     * Registers a new user.
     *
     * @param request The registration request containing user details.
     * @return true if registration was successful, false otherwise.
     */
    public boolean registerUser(RegisterRequest request) {
        Optional<User> existingUser = userRepository.findByEmail(request.getEmail());
        if (existingUser.isPresent()) {
            return false; // User already exists
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setActive(true);
        user.setLocked(false);
        user.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
        userRepository.save(user);
        return true;
    }

    /**
     * Authenticates a user and issues an access token.
     *
     * @param request The login request containing user credentials.
     * @return Access token if authentication is successful, null otherwise.
     */
    public String authenticateUser(LoginRequest request) {
        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                return jwtService.generateAccessToken(userDetailsService.loadUserByUsername(user.getEmail()));
            }
        }
        return null; // Invalid credentials
    }

    /**
     * Handles OAuth login and generates OAuth token.
     *
     * @param request The OAuth login request containing OAuth credentials.
     * @return OAuth token if successful, null otherwise.
     */
    public String authenticateOAuth(OAuthRequest request) {
        // Placeholder for OAuth authentication logic
        // This should interact with an OAuth provider like Google, Facebook, etc.
        // For now, we simulate it with a success message.
        if (request.getOauthToken() != null && !request.getOauthToken().isEmpty()) {
            return jwtService.generateAccessToken(userDetailsService.loadUserByUsername(request.getOauthToken()));
        }
        return null;
    }

    /**
     * Sends a password reset email to the user.
     *
     * @param request The reset request containing the user's email.
     * @return true if email was sent successfully, false otherwise.
     */
    public boolean sendPasswordResetEmail(ResetRequest request) {
        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());
        if (userOptional.isPresent()) {
            // Generate the reset link (in this case, just a placeholder)
            String resetLink = "http://localhost:8080/reset-password?token=sampleToken";
            String subject = "Reset Password";
            String body = "Your password has been reset!";
            return emailUtil.sendPasswordResetEmail(request.getEmail(), subject, body, resetLink);
        }
        return false; // User not found
    }

    /**
     * Resets the user's password using the provided token.
     *
     * @param request The reset password request containing token and new password.
     * @return true if the password was successfully reset, false otherwise.
     */
    public boolean resetUserPassword(ResetPasswordRequest request) {
        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            userRepository.save(user);
            return true;
        }
        return false; // User not found or invalid token
    }

    /**
     * Checks if the user is authenticated by validating the token from the request.
     *
     * @param request The HTTP request containing the token.
     * @return true if the user is authenticated, false otherwise.
     */
    public boolean isAuthenticated(HttpServletRequest request) {
        final String token = extractTokenFromHeader(request);
        if (token == null || !jwtService.isTokenValid(token)) {
            return false;
        }

        final String username = jwtService.extractUsername(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return jwtService.isTokenValid(token, userDetails);
    }

    /**
     * Extracts the token from the HTTP request header.
     *
     * @param request The HTTP request.
     * @return The token if present, null otherwise.
     */
    private String extractTokenFromHeader(HttpServletRequest request) {
        final String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7); // remove "Bearer "
        }
        return null;
    }

    public String getUsername(HttpServletRequest request) {
        final String token = extractTokenFromHeader(request);
        if (token == null || !jwtService.isTokenValid(token)) {
            return null;
        }
        return jwtService.extractUsername(token);
    }

    public UserDetails getUserDetails(String username) {
        try {
            return userDetailsService.loadUserByUsername(username);
        } catch (Exception e) {
            return null;
        }
    }
}
