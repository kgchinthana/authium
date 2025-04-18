package com.secure.authium.dtos.outputs;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class TokenResponse {
    private String accessToken;
    private String refreshToken;
}

