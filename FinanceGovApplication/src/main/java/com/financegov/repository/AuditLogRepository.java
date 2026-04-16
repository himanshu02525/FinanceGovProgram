package com.financegov.repository;

import com.financegov.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    List<AuditLog> findByActorEmail(String email);
    List<AuditLog> findByAction(String action);
}