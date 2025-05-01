package com.secure.authium.services;

import com.secure.authium.dtos.inputs.DelegateRequest;
import com.secure.authium.dtos.inputs.RoleRequest;
import com.secure.authium.dtos.inputs.RoleUpdateRequest;
import com.secure.authium.dtos.outputs.AuditLogResponse;
import com.secure.authium.dtos.outputs.UpdateUserResponse;
import com.secure.authium.dtos.outputs.UserResponse;
import com.secure.authium.models.AuditLog;
import com.secure.authium.models.Role;
import com.secure.authium.models.User;
import com.secure.authium.models.UserRole;
import com.secure.authium.repositories.AuditLogRepository;
import com.secure.authium.repositories.RoleRepository;
import com.secure.authium.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

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
                .map(user -> {
                    List<String> permissionNames = userRepository.findPermissionNamesByUsername(user.getUsername());
                    return UserResponse.fromEntity(user, permissionNames);
                })
                .toList();
    }

    @Transactional
    public UpdateUserResponse updateUserRoles(Long userId, RoleUpdateRequest request, String adminUsername) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Role role = roleRepository.findByName(request.getRoleName())
                .orElseThrow(() -> new RuntimeException("Role not found: " + request.getRoleName()));

        user.getUserRoles().clear();

        UserRole userRole = new UserRole();
        userRole.setUser(user);
        userRole.setRole(role);
        user.getUserRoles().add(userRole);

        userRepository.save(user);

        logAction(adminUsername, "User roles updated successfully");

        return UpdateUserResponse.fromEntity(user);
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

        Role role = roleRepository.findByName(request.getRole())
                .orElseThrow(() -> new RuntimeException("Role not found: " + request.getRole()));

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

        logAction(adminUsername, "Permissions delegated: " + role.getName());

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
        List<String> permissionNames = userRepository.findPermissionNamesByUsername(user.getUsername());
        return UserResponse.fromEntity(user, permissionNames);
    }



}
