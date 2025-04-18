package com.secure.authium.repositories;

import com.secure.authium.models.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

}
