package com.parkease.backend.controller;

import com.parkease.backend.service.AdminNotificationService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/notifications")
@PreAuthorize("hasRole('ADMIN')")
public class AdminNotificationController {

    private final AdminNotificationService service;

    public AdminNotificationController(AdminNotificationService service) {
        this.service = service;
    }

    @GetMapping("/count")
    public long unreadCount() {
        return service.getUnreadCount();
    }
}
