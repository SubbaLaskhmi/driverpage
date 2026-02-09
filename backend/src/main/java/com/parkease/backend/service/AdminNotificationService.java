package com.parkease.backend.service;

import com.parkease.backend.repository.NotificationRepository;
import org.springframework.stereotype.Service;

@Service
public class AdminNotificationService {

    private final NotificationRepository repository;

    public AdminNotificationService(NotificationRepository repository) {
        this.repository = repository;
    }

    public long getUnreadCount() {
        return repository.countByTargetRoleAndIsReadFalse("ADMIN");
    }
}
