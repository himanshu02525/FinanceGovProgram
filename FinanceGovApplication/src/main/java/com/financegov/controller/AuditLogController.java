package com.financegov.controller;

import com.financegov.entity.AuditLog;
import com.financegov.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/audit")
@RequiredArgsConstructor
public class AuditLogController {

    private final AuditLogRepository auditLogRepository;

    /**
     * Fetches every single action recorded in the system.
     * Restricted to ADMIN or GOVERNMENT_AUDITOR.
     */
    @GetMapping("/logs")
    @PreAuthorize("hasAnyRole('ADMIN', 'GOVERNMENT_AUDITOR')")
    public ResponseEntity<List<AuditLog>> getAllLogs() {
        return ResponseEntity.ok(auditLogRepository.findAll());
    }

    /**
     * Filter logs by a specific user (e.g., see everything 'ritesh@gov.in' did).
     */
    @GetMapping("/logs/user/{email}")
    @PreAuthorize("hasAnyRole('ADMIN', 'GOVERNMENT_AUDITOR')")
    public ResponseEntity<List<AuditLog>> getLogsByUser(@PathVariable String email) {
        return ResponseEntity.ok(auditLogRepository.findByActorEmail(email));
    }

    /**
     * Filter logs by action (e.g., see all 'USER_LOGOUT' events).
     */
    @GetMapping("/logs/action/{action}")
    @PreAuthorize("hasAnyRole('ADMIN', 'GOVERNMENT_AUDITOR')")
    public ResponseEntity<List<AuditLog>> getLogsByAction(@PathVariable String action) {
        return ResponseEntity.ok(auditLogRepository.findByAction(action));
    }
}