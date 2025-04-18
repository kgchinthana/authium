package com.secure.authium.endpoint;

import com.secure.authium.dtos.inputs.UpdateUserRequest;
import com.secure.authium.dtos.outputs.UpdateUserResponse;
import com.secure.authium.dtos.outputs.UserResponse;
import com.secure.authium.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Endpoint responsible for user-related operations such as fetching and updating profiles.
 */
@Component
public class UserEndpoint {

    private final UserService userService;

    @Autowired
    public UserEndpoint(UserService userService) {
        this.userService = userService;
    }

    /**
     * Fetches the profile of the user by username.
     *
     * @param username Username of the user
     * @return UserResponse DTO containing profile data
     */
    public UserResponse getProfile(String username) {
        return userService.getProfile(username);
    }

    /**
     * Updates the user profile based on the given request.
     *
     * @param username       Username of the user
     * @param updateRequest  Data to update
     */
    public UpdateUserResponse updateUser(String username, UpdateUserRequest updateRequest) {
        return userService.updateUser(username, updateRequest);
    }
}