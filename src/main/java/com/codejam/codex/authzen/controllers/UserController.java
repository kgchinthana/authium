package com.codejam.codex.authzen.controllers;


import com.codejam.codex.authzen.dtos.inputs.UpdateUserRequest;
import com.codejam.codex.authzen.models.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@PreAuthorize("hasRole('USER')")
public class UserController {

    @GetMapping("/me")
    public ResponseEntity<?> getProfile() {
        // return user details
        return null;
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateProfile(@RequestBody UpdateUserRequest request) {
        // update user info
        return null;
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        // invalidate token/session
        return null;
    }
}
