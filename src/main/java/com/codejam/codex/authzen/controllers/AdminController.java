package com.codejam.codex.authzen.controllers;

import com.codejam.codex.authzen.constants.ApiEndpoint;
import com.codejam.codex.authzen.dtos.inputs.DelegateRequest;
import com.codejam.codex.authzen.dtos.inputs.RoleRequest;
import com.codejam.codex.authzen.dtos.inputs.RoleUpdateRequest;
import com.codejam.codex.authzen.dtos.outputs.UserResponse;
import com.codejam.codex.authzen.endpoint.UserEndpoint;
import com.codejam.codex.authzen.models.AuditLog;
import com.codejam.codex.authzen.models.User;
import com.codejam.codex.authzen.responses.AuthzenResponse;
import com.codejam.codex.authzen.services.AdminService;
import com.codejam.codex.authzen.services.AuthService;
import com.codejam.codex.authzen.utils.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiEndpoint.ADMIN)
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;
    private final AuthService authService;

    @Autowired
    public AdminController(AdminService adminService, AuthService authService) {
        this.adminService = adminService;
        this.authService = authService;
    }

    /**
     * Fetch all users - Only accessible by admins
     */
    @GetMapping("/users")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<AuthzenResponse<List<UserResponse>>> getAllUsers(HttpServletRequest request) {
        String username = authService.getUsername(request);
        if (username == null || !authService.isAuthenticated(request)) {
            return ResponseEntity.status(401).body(null); // Unauthorized if no valid token is found
        }

        List<UserResponse> users = adminService.getAllUsers(username);
        return ResponseEntity.ok(new AuthzenResponse<>(users));

    }

    /**
     * Get a user by ID - Only accessible by admins
     */
    @GetMapping("/users/{id}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<User> getUserDetails(@PathVariable("id") Long userId, HttpServletRequest request) {
        String username = authService.getUsername(request);
        if (username == null || !authService.isAuthenticated(request)) {
            return ResponseEntity.status(401).body(null); // Unauthorized if no valid token is found
        }

        User user = adminService.getUserById(userId);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(404).body(null); // Not Found if user doesn't exist
        }
    }

    /**
     * Update a user's role - Only accessible by admins
     */
    @PutMapping("/users/{id}/roles")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<String> updateUserRole(@PathVariable("id") Long userId,
                                                 @RequestBody RoleUpdateRequest request,
                                                 HttpServletRequest httpRequest) {
        String username = authService.getUsername(httpRequest);
        if (username == null || !authService.isAuthenticated(httpRequest)) {
            return ResponseEntity.status(401).body("Unauthorized: No token provided.");
        }

        UserResponse userResponse = authService.getUserDetails(username);
        if (userResponse == null) {
            return ResponseEntity.status(401).body("Unauthorized: Invalid user.");
        }

        // Check if the user has permission to change roles
        if (!userResponse.getRoles().contains("ROLE_ADMIN")) {
            return ResponseEntity.status(403).body("Forbidden: Insufficient permissions.");
        }

        String updated = adminService.updateUserRoles(userId, request, username);
        return ResponseEntity.ok(updated);
    }

    /**
     * Create a new role - Only accessible by admins
     */
    @PostMapping("/roles")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<String> createRole(@RequestBody RoleRequest roleRequest, HttpServletRequest request) {
        String username = authService.getUsername(request);
        if (username == null || !authService.isAuthenticated(request)) {
            return ResponseEntity.status(401).body("Unauthorized: No token provided.");
        }

        UserResponse userResponse = authService.getUserDetails(username);
        if (userResponse == null) {
            return ResponseEntity.status(401).body("Unauthorized: Invalid user.");
        }

        // Check if the user has permission to create roles
        if (!userResponse.getRoles().contains("ROLE_ADMIN")) {
            return ResponseEntity.status(403).body("Forbidden: Insufficient permissions.");
        }

        String created = adminService.createRole(roleRequest, username);
        return ResponseEntity.ok(created);

    }

    /**
     * Fetch audit logs - Only accessible by admins
     */
    @GetMapping("/audit-logs")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<String> getAuditLogs(HttpServletRequest request) {
        String username = authService.getUsername(request);
        if (username == null || !authService.isAuthenticated(request)) {
            return ResponseEntity.status(401).body("Unauthorized: No token provided.");
        }

        UserResponse userResponse = authService.getUserDetails(username);
        if (userResponse == null) {
            return ResponseEntity.status(401).body("Unauthorized: Invalid user.");
        }

        // Check if the user has permission to view audit logs
        if (!userResponse.getRoles().contains("ROLE_ADMIN")) {
            return ResponseEntity.status(403).body("Forbidden: Insufficient permissions.");
        }

        // Placeholder: Fetch and return the audit logs
        List<AuditLog> auditLogs = adminService.getAuditLogs(username);

        return ResponseEntity.ok(auditLogs.toString());
    }

    /**
     * Delegate permissions to another user - Only accessible by admins
     */
    @PostMapping("/delegate")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<String> delegatePermissions(@RequestBody DelegateRequest delegateRequest,
                                                      HttpServletRequest request) {
        String username = authService.getUsername(request);
        if (username == null || !authService.isAuthenticated(request)) {
            return ResponseEntity.status(401).body("Unauthorized: No token provided.");
        }

        UserResponse userResponse = authService.getUserDetails(username);
        if (userResponse == null) {
            return ResponseEntity.status(401).body("Unauthorized: Invalid user.");
        }

        // Check if the user has permission to delegate roles
        if (!userResponse.getRoles().contains("ROLE_ADMIN")) {
            return ResponseEntity.status(403).body("Forbidden: Insufficient permissions.");
        }

        // Handle permission delegation logic
        String delegated = adminService.delegatePermissions(delegateRequest, username);

        return ResponseEntity.ok(delegated);


    }
}