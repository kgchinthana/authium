package com.codejam.codex.authzen.services;

import com.codejam.codex.authzen.dtos.inputs.DelegateRequest;
import com.codejam.codex.authzen.dtos.inputs.RoleRequest;
import com.codejam.codex.authzen.dtos.inputs.RoleUpdateRequest;
import com.codejam.codex.authzen.dtos.outputs.UserResponse;
import com.codejam.codex.authzen.models.AuditLog;
import com.codejam.codex.authzen.models.Role;
import com.codejam.codex.authzen.models.User;
import com.codejam.codex.authzen.repositories.AuditLogRepository;
import com.codejam.codex.authzen.repositories.RoleRepository;
import com.codejam.codex.authzen.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {
//
//    private final UserRepository userRepository;
//    private final RoleRepository roleRepository;
//    private final AuditLogRepository auditLogRepository;
//
//    public List<UserResponse> getAllUsers(String adminUsername) {
//        logAction(adminUsername, "Fetched all users");
//        return userRepository.findAll()
//                .stream()
//                .map(UserResponse::fromEntity)
//                .toList();
//    }
//
//    @Transactional
//    public String updateUserRoles(Long userId, RoleUpdateRequest request, String adminUsername) {
//        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
//        List<Role> roles = roleRepository.findByNameIn((request.getRoleName()));
//        user.setUserRoles.roles);
//        userRepository.save(user);
//        logAction(adminUsername, "Updated roles for user ID " + userId);
//        return "User roles updated successfully";
//    }
//
//    public List<AuditLog> getAuditLogs(String adminUsername) {
//        logAction(adminUsername, "Viewed audit logs");
//        return auditLogRepository.findAll();
//    }
//
//    public String createRole(RoleRequest request, String adminUsername) {
//        if (roleRepository.existsByName(request.getRoleName())) {
//            throw new IllegalArgumentException("Role already exists");
//        }
//        Role role = new Role();
//        role.setName(request.getRole());
//        roleRepository.save(role);
//        logAction(adminUsername, "Created new role: " + request.getRole());
//        return "Role created successfully";
//    }
//
//    public String delegatePermissions(DelegateRequest request, String adminUsername) {
//        User user = userRepository.findById(request.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));
//        Role role = roleRepository.findByName(request.getRole()).orElseThrow(() -> new RuntimeException("Role not found"));
//        user.getRoles().add(role);
//        userRepository.save(user);
//        logAction(adminUsername, "Delegated role " + request.getRole() + " to user ID " + request.getUserId());
//        return "Permissions delegated successfully";
//    }
//
//    private void logAction(String adminUsername, String action) {
//        AuditLog log = new AuditLog();
//        log.setAdminUsername(adminUsername);
//        log.setAction(action);
//        log.setTimestamp(Instant.now());
//        auditLogRepository.save(log);
//    }
}
