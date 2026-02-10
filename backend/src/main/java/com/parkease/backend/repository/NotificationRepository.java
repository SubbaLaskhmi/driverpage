package com.parkease.backend.repository;

import com.parkease.backend.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    /* ================= ADMIN ================= */
    List<Notification> findByTargetRoleOrderByCreatedAtDesc(String targetRole);
    long countByTargetRoleAndIsReadFalse(String targetRole);

    /* ================= PROVIDER ================= */
    List<Notification> findByTargetUserIdOrderByCreatedAtDesc(Long targetUserId);
   List<Notification> findByTargetRoleAndTargetUserIdOrderByCreatedAtDesc(
        String targetRole,
        Long targetUserId
);

long countByTargetRoleAndTargetUserIdAndIsReadFalse(
        String targetRole,
        Long targetUserId
);

    long countByTargetUserIdAndIsReadFalse(Long targetUserId);
}
