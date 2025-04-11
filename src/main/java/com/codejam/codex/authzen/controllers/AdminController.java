package com.codejam.codex.authzen.controllers;

import com.codejam.codex.authzen.dtos.inputs.DelegateRequest;
import com.codejam.codex.authzen.dtos.inputs.RoleRequest;
import com.codejam.codex.authzen.dtos.inputs.RoleUpdateRequest;
import com.codejam.codex.authzen.models.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        // return list of users
        return null;
    }

    @PutMapping("/users/{id}/roles")
    public ResponseEntity<?> updateUserRoles(@PathVariable Long id, @RequestBody RoleUpdateRequest request) {
        // assign roles to user
        return null;
    }

    @GetMapping("/audit-logs")
    public ResponseEntity<?> getAuditLogs() {
        // return system logs
        return null;
    }

    @PostMapping("/roles")
    public ResponseEntity<?> createRole(@RequestBody RoleRequest request) {
        // create new role
        return null;
    }

    @PostMapping("/delegate")
    public ResponseEntity<?> delegatePermissions(@RequestBody DelegateRequest request) {
        // delegate permission/role
        return null;
    }
}

