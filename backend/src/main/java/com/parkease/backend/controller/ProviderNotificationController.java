package com.parkease.backend.controller;

import com.parkease.backend.entity.Notification;
import com.parkease.backend.entity.User;
import com.parkease.backend.repository.NotificationRepository;
import com.parkease.backend.repository.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/provider/notifications")
@PreAuthorize("hasRole('PROVIDER')")
public class ProviderNotificationController {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public ProviderNotificationController(
            NotificationRepository notificationRepository,
            UserRepository userRepository
    ) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }

    /* ================= GET ALL PROVIDER NOTIFICATIONS ================= */
    @GetMapping
    public List<Notification> getNotifications(Authentication auth) {

        User provider = userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Provider not found"));

        return notificationRepository
                .findByTargetUserIdOrderByCreatedAtDesc(provider.getId());
    }

    /* ================= UNREAD COUNT (BADGE) ================= */
    @GetMapping("/unread-count")
    public long unreadCount(Authentication auth) {

        User provider = userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Provider not found"));

        return notificationRepository
                .countByTargetUserIdAndIsReadFalse(provider.getId());
    }

    /* ================= MARK AS READ ================= */
    @PutMapping("/{id}/read")
    public void markAsRead(@PathVariable Long id) {

        Notification n = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        n.setRead(true);
        notificationRepository.save(n);
    }
}
