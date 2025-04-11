package com.codejam.codex.authzen.controllers;


import com.codejam.codex.authzen.constants.ApiEndpoint;
import com.codejam.codex.authzen.dtos.inputs.UpdateUserRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiEndpoint.USER)
@PreAuthorize("hasRole('USER')")
public class UserController {

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
}
