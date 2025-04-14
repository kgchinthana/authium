package com.codejam.codex.authzen.dtos.outputs;

import com.codejam.codex.authzen.models.User;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private Set<String> roles;

    public static UserResponse fromEntity(User user) {
        return UserResponse.builder().id(user.getId()).username(user.getUsername()).email(user.getEmail()).build();
    }
}
