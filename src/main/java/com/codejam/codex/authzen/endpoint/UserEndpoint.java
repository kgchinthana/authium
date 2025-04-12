package com.codejam.codex.authzen.endpoint;

import com.codejam.codex.authzen.dtos.inputs.UpdateUserRequest;
import com.codejam.codex.authzen.dtos.outputs.UserResponse;
import com.codejam.codex.authzen.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserEndpoint {
    private final UserService userService;

    @Autowired
    public UserEndpoint(UserService userService) {
        this.userService = userService;
    }

    public UserResponse getProfile(String username) {
        return userService.getProfile(username);
    }

    public void updateUser(String username, UpdateUserRequest updateRequest) {
        userService.updateUser(username, updateRequest);
    }
}
