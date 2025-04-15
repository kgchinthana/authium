package com.codejam.codex.authzen.services;

import com.codejam.codex.authzen.dtos.inputs.DelegateRequest;
import com.codejam.codex.authzen.dtos.inputs.RoleRequest;
import com.codejam.codex.authzen.dtos.inputs.RoleUpdateRequest;
import com.codejam.codex.authzen.dtos.outputs.AuditLogResponse;
import com.codejam.codex.authzen.dtos.outputs.UserResponse;
import com.codejam.codex.authzen.models.AuditLog;
import com.codejam.codex.authzen.models.Role;
import com.codejam.codex.authzen.models.User;
import com.codejam.codex.authzen.models.UserRole;
import com.codejam.codex.authzen.repositories.AuditLogRepository;
import com.codejam.codex.authzen.repositories.RoleRepository;
import com.codejam.codex.authzen.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AuditLogRepository auditLogRepository;

    public List<UserResponse> getAllUsers(String adminUsername) {
        logAction(adminUsername, "User list got successfully");
        return userRepository.findAll()
                .stream()
                .map(UserResponse::fromEntity)
                .toList();
    }

    @Transactional
    public String updateUserRoles(Long userId, RoleUpdateRequest request, String adminUsername) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Role> roles = roleRepository.findByName(request.getRoleName());

        // Clear existing roles
        user.getUserRoles().clear();

        // Assign new roles
        for (Role role : roles) {
            UserRole userRole = new UserRole();
            userRole.setUser(user);
            userRole.setRole(role);
            user.getUserRoles().add(userRole);
        }

        userRepository.save(user);
        logAction(adminUsername, "User roles updated successfully");
        return "User roles updated successfully";
    }


    public List<AuditLogResponse> getAuditLogs(String adminUsername) {
        List<AuditLog> auditLogs = auditLogRepository.findAll();

        return auditLogs.stream()
                .map(log -> AuditLogResponse.builder()
                        .id(log.getId())
                        .username(log.getUser().getUsername())
                        .actionType(log.getActionType())
                        .ipAddress(log.getIpAddress())
                        .timestamp(log.getTimestamp())
                        .build())
                .toList();
    }


    public String createRole(RoleRequest request, String adminUsername) {
        String roleName = request.getRoleName();

        if (roleRepository.existsByName(roleName)) {
            throw new IllegalArgumentException("Role already exists");
        }

        Role role = new Role();
        role.setName(roleName);
        role.setDescription(request.getDescription());
        roleRepository.save(role);

        logAction(adminUsername, "Role created successfully");

        return "Role created successfully";
    }


    public String delegatePermissions(DelegateRequest request, String adminUsername) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Role> roles = roleRepository.findByName(request.getRole());
        if (roles.isEmpty()) {
            throw new RuntimeException("Role not found");
        }
        Role role = roles.get(0); // or apply some selection logic


        // Check if the user already has the role to prevent duplicates
        boolean alreadyAssigned = user.getUserRoles().stream()
                .anyMatch(userRole -> userRole.getRole().getName().equals(role.getName()));
        if (alreadyAssigned) {
            return "User already has this role";
        }

        UserRole userRole = new UserRole();
        userRole.setUser(user);
        userRole.setRole(role);

        user.getUserRoles().add(userRole);
        userRepository.save(user);
        logAction(adminUsername, "Permissions delegated successfully");

        return "Permissions delegated successfully";
    }


    private void logAction(String adminUsername, String actionType) {
        User adminUser = userRepository.findByUsername(adminUsername)
                .orElseThrow(() -> new RuntimeException("Admin user not found"));

        AuditLog log = new AuditLog();
        log.setUser(adminUser);
        log.setActionType(actionType);
        log.setTimestamp(new Timestamp(System.currentTimeMillis()));

        auditLogRepository.save(log);
    }


    public UserResponse getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        return UserResponse.fromEntity(user);
    }



}
