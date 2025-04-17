package com.secure.authium.dtos.outputs;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuditLogResponse {
    private Long id;
    private String username;
    private String actionType;
    private String ipAddress;
    private Timestamp timestamp;
}
