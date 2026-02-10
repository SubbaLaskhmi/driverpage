package com.parkease.backend.controller;

import com.parkease.backend.entity.Notification;
import com.parkease.backend.repository.NotificationRepository;
import com.parkease.backend.service.NotificationService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/notifications")
@PreAuthorize("hasRole('ADMIN')")
public class AdminNotificationController {

    private final NotificationRepository notificationRepository;
    private final NotificationService notificationService;

    public AdminNotificationController(
            NotificationRepository notificationRepository,
            NotificationService notificationService
    ) {
        this.notificationRepository = notificationRepository;
        this.notificationService = notificationService;
    }

    @GetMapping
    public List<Notification> getAdminNotifications() {
        return notificationRepository
                .findByTargetRoleOrderByCreatedAtDesc("ADMIN");
    }

    @GetMapping("/unread-count")
    public long unreadCount() {
        return notificationRepository
                .countByTargetRoleAndIsReadFalse("ADMIN");
    }

    @PutMapping("/{id}/read")
    public void markRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
    }
}
