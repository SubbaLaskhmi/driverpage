package com.parkease.backend.controller;

import com.parkease.backend.dto.AdminAuditLogResponse;
import com.parkease.backend.service.AdminAuditLogService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/audit-logs")
@PreAuthorize("hasRole('ADMIN')")
public class AdminAuditLogController {

    private final AdminAuditLogService auditLogService;

    public AdminAuditLogController(AdminAuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    /* =====================================================
       GET AUDIT LOGS (ADMIN UI)
       ===================================================== */
    @GetMapping
    public List<AdminAuditLogResponse> getAuditLogs() {
        return auditLogService.getAllLogs();
    }
}
