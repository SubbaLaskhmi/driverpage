package com.parkease.backend.repository;

import com.parkease.backend.entity.AdminAuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdminAuditLogRepository
        extends JpaRepository<AdminAuditLog, Long> {

    /* ================= ADMIN HISTORY ================= */

    List<AdminAuditLog> findByAdminIdOrderByTimestampDesc(Long adminId);

    /* ================= PROVIDER HISTORY ================= */

    List<AdminAuditLog> findByProviderIdOrderByTimestampDesc(Long providerId);
    
    List<AdminAuditLog> findAllByOrderByTimestampDesc();
}
