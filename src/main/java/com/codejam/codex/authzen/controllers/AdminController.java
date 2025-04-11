package com.codejam.codex.authzen.controllers;

import com.codejam.codex.authzen.constants.ApiEndpoint;
import com.codejam.codex.authzen.dtos.inputs.DelegateRequest;
import com.codejam.codex.authzen.dtos.inputs.RoleRequest;
import com.codejam.codex.authzen.dtos.inputs.RoleUpdateRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiEndpoint.ADMIN)
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @GetMapping(ApiEndpoint.ADMIN_USERS)
    public ResponseEntity<?> getAllUsers() {
        // return list of users
        return null;
    }

    @PutMapping(ApiEndpoint.ADMIN_USER_ROLES)
    public ResponseEntity<?> updateUserRoles(@PathVariable Long id, @RequestBody RoleUpdateRequest request) {
        // assign roles to user
        return null;
    }

    @GetMapping(ApiEndpoint.ADMIN_AUDIT_LOGS)
    public ResponseEntity<?> getAuditLogs() {
        // return system logs
        return null;
    }

    @PostMapping(ApiEndpoint.ADMIN_ROLES)
    public ResponseEntity<?> createRole(@RequestBody RoleRequest request) {
        // create new role
        return null;
    }

    @PostMapping(ApiEndpoint.ADMIN_DELEGATE)
    public ResponseEntity<?> delegatePermissions(@RequestBody DelegateRequest request) {
        // delegate permission/role
        return null;
    }
}

