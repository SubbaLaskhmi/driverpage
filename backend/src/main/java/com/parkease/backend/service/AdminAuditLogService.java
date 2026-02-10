package com.parkease.backend.service;

import com.parkease.backend.dto.AdminAuditLogResponse;
import com.parkease.backend.entity.AdminAuditLog;
import com.parkease.backend.repository.AdminAuditLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class AdminAuditLogService {

    private final AdminAuditLogRepository auditLogRepository;

    public AdminAuditLogService(AdminAuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    /* =====================================================
       LOG ACTION (CALLED INTERNALLY)
       ===================================================== */
    public void log(
            Long adminId,
            Long providerId,
            String providerName,
            String action,
            String reason
    ) {
        AdminAuditLog log = new AdminAuditLog();
        log.setAdminId(adminId);
        log.setProviderId(providerId);
        log.setProviderName(providerName);
        log.setAction(action);
        log.setReason(reason);
        log.setTimestamp(LocalDateTime.now());

        auditLogRepository.save(log);
    }

    /* =====================================================
       FETCH FOR ADMIN UI
       ===================================================== */
    public List<AdminAuditLogResponse> getAllLogs() {
        return auditLogRepository.findAllByOrderByTimestampDesc()
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    private AdminAuditLogResponse mapToDto(AdminAuditLog log) {
        AdminAuditLogResponse dto = new AdminAuditLogResponse();
        dto.setId(log.getId());
        dto.setProviderName(log.getProviderName());
        dto.setAction(log.getAction());
        dto.setReason(log.getReason());
        dto.setTimestamp(log.getTimestamp());
        return dto;
    }
}
