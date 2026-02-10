package com.parkease.backend.service;

import com.parkease.backend.entity.Notification;
import com.parkease.backend.entity.User;
import com.parkease.backend.repository.NotificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    /* =====================================================
       ADMIN NOTIFICATIONS
       ===================================================== */

    public void notifyAdmin(String message) {
        Notification n = new Notification();
        n.setMessage(message);
        n.setTargetRole("ADMIN");
        n.setRead(false);
        n.setCreatedAt(LocalDateTime.now());

        notificationRepository.save(n);
    }

    /* =====================================================
       PROVIDER NOTIFICATIONS (BY PROVIDER ID)
       ===================================================== */

    public void notifyProvider(Long providerId, String message) {
        Notification n = new Notification();
        n.setMessage(message);
        n.setTargetRole("PROVIDER");
        n.setTargetUserId(providerId);
        n.setRead(false);
        n.setCreatedAt(LocalDateTime.now());

        notificationRepository.save(n);
    }

    /* =====================================================
       PROVIDER NOTIFICATIONS (BY USER ENTITY) ✅ NEW
       ===================================================== */

    public void notifyProvider(User provider, String message) {
        if (provider == null || provider.getId() == null) {
            throw new RuntimeException("Invalid provider");
        }

        notifyProvider(provider.getId(), message);
    }

    /* =====================================================
       MARK AS READ
       ===================================================== */

    public void markAsRead(Long notificationId) {
        Notification n = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        n.setRead(true);
        notificationRepository.save(n);
    }
}
