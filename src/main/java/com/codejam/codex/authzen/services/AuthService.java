package com.codejam.codex.authzen.services;

import com.codejam.codex.authzen.dtos.inputs.*;
import com.codejam.codex.authzen.dtos.outputs.TokenResponse;
import com.codejam.codex.authzen.dtos.outputs.UserResponse;
import com.codejam.codex.authzen.models.EmailToken;
import com.codejam.codex.authzen.models.OauthProvider;
import com.codejam.codex.authzen.models.User;
import com.codejam.codex.authzen.repositories.EmailTokenRepository;
import com.codejam.codex.authzen.repositories.OauthProviderRepository;
import com.codejam.codex.authzen.repositories.UserRepository;
import com.codejam.codex.authzen.utils.EmailUtil;
import com.codejam.codex.authzen.utils.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;


@Service
public class AuthService {

    private final JwtService jwtService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final EmailUtil emailUtil;
    private final EmailTokenRepository emailTokenRepository;
    private final OauthProviderRepository oauthProviderRepository;
    private final OAuthService oAuthService;

    @Autowired
    public AuthService(JwtService jwtService, UserService userService, UserRepository userRepository,
                       BCryptPasswordEncoder passwordEncoder, EmailUtil emailUtil, EmailTokenRepository emailTokenRepository, OauthProviderRepository oauthProviderRepository, OAuthService oauthService, OAuthService oAuthService) {
        this.jwtService = jwtService;
        this.userService = userService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailUtil = emailUtil; // Injected
        this.emailTokenRepository = emailTokenRepository;
        this.oauthProviderRepository = oauthProviderRepository;
        this.oAuthService = oAuthService;
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
    public TokenResponse authenticateUser(LoginRequest request) {
        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                UserResponse userResponse = userService.loadUserByUsername(user.getEmail());
                String accessToken = jwtService.generateAccessToken(userResponse);
                String refreshToken = jwtService.generateRefreshToken(userResponse);
                return new TokenResponse(accessToken, refreshToken);
            }
        }
        return null;
    }

    /**
     * Handles OAuth login and generates OAuth token.
     *
     * @param request The OAuth login request containing OAuth credentials.
     * @return OAuth token if successful, null otherwise.
     */
    public TokenResponse authenticateOAuth(OAuthRequest request) {
        if ("github".equalsIgnoreCase(request.getProvider())) {
            String accessToken = oAuthService.getGithubAccessToken(request.getOauthToken());
            Map<String, Object> githubUser = oAuthService.getGithubUser(accessToken);

            String githubId = githubUser.get("id").toString();
            String githubEmail = (String) githubUser.get("email");
            String githubLogin = (String) githubUser.get("login");

            // Check if provider mapping exists
            Optional<OauthProvider> providerOpt = oauthProviderRepository.findByProviderAndExternalUserId("github", githubId);

            User user;
            if (providerOpt.isPresent()) {
                user = providerOpt.get().getUser();
            } else {
                // Check by email (if exists)
                user = userRepository.findByEmail(githubEmail)
                        .orElseGet(() -> userRepository.save(User.builder()
                                .username(githubLogin)
                                .email(githubEmail)
                                .password("") // no password
                                .isActive(true)
                                .isLocked(false)
                                .build()));

                oauthProviderRepository.save(OauthProvider.builder()
                        .provider("github")
                        .externalUserId(githubId)
                        .user(user)
                        .build());
            }

            UserResponse userResponse = userService.loadUserByUsername(user.getEmail());
            return new TokenResponse(
                    jwtService.generateAccessToken(userResponse),
                    jwtService.generateRefreshToken(userResponse)
            );
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
            User user = userOptional.get();

            EmailToken token = EmailToken.builder()
                    .user(user)
                    .token(UUID.randomUUID().toString())
                    .purpose("RESET_PASSWORD")
                    .expiresAt(Timestamp.from(Instant.now().plus(15, ChronoUnit.MINUTES)))
                    .build();

            emailTokenRepository.save(token);

            String resetLink = "http://localhost:8080/reset-password/reset-password.html?token=" + token.getToken();

            String subject = "Password Reset Request";
            String body = "You have requested to reset your password. Click the link below to reset your password:\n" + resetLink;

            return emailUtil.sendPasswordResetEmail(request.getEmail(), subject, body, resetLink);
        }
        return false;
    }


    /**
     * Resets the user's password using the provided token.
     *
     * @param request The reset password request containing token and new password.
     * @return true if the password was successfully reset, false otherwise.
     */
    public boolean resetUserPassword(ResetPasswordRequest request) {
        Optional<EmailToken> tokenOptional = emailTokenRepository.findByToken(request.getToken());
        if (tokenOptional.isPresent()) {
            EmailToken token = tokenOptional.get();

            if (token.getExpiresAt().before(Timestamp.from(Instant.now()))) {
                return false;
            }

            if (!"RESET_PASSWORD".equals(token.getPurpose())) {
                return false;
            }

            Optional<User> userOptional = userRepository.findByEmail(request.getEmail());
            if (userOptional.isPresent()) {
                User user = userOptional.get();

                if (!user.equals(token.getUser())) {
                    return false;
                }

                user.setPassword(passwordEncoder.encode(request.getNewPassword()));
                userRepository.save(user);

                emailTokenRepository.delete(token);

                return true;
            }
        }
        return false;
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
        UserResponse userDetails = userService.loadUserByUsername(username);
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

    public UserResponse getUserDetails(String username) {
        try {
            return userService.loadUserByUsername(username);
        } catch (Exception e) {
            return null;
        }
    }
}
