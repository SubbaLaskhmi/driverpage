package com.parkease.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.parkease.backend.entity.Notification;
import com.parkease.backend.repository.NotificationRepository;

@RestController
@RequestMapping("/api/admin/notifications")
@PreAuthorize("hasRole('ADMIN')")
public class NotificationController {

    private final NotificationRepository notificationRepository;

    public NotificationController(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    /* =====================================================
       GET ADMIN NOTIFICATIONS
       ===================================================== */
    @GetMapping
    public ResponseEntity<List<Notification>> getAdminNotifications() {
        List<Notification> notifications =
                notificationRepository.findByTargetRoleOrderByCreatedAtDesc("ADMIN");
        return ResponseEntity.ok(notifications);
    }

    /* =====================================================
       MARK NOTIFICATION AS READ
       ===================================================== */
    @PutMapping("/{id}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long id) {
        Notification n = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        n.setRead(true);
        notificationRepository.save(n);
        return ResponseEntity.ok().build();
    }
}
