package com.parkease.backend.repository;

import com.parkease.backend.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    // ✅ ROLE-BASED NOTIFICATIONS
    List<Notification> findByTargetRoleOrderByCreatedAtDesc(String targetRole);

    // ✅ FIXED: match entity field `isRead`
    long countByTargetRoleAndIsReadFalse(String targetRole);
}
