package com.parkease.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 500)
    private String message;

    // ADMIN / PROVIDER
    @Column(nullable = false)
    private String targetRole;

    
    // Nullable → used for provider-specific notifications
    private Long targetUserId;

    @Column(nullable = false)
    private boolean isRead = false;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    /* ================= CONSTRUCTORS ================= */

    public Notification() {
        // JPA requires empty constructor
    }

    // ✅ THIS IS WHAT YOUR SERVICE NEEDS
    public Notification(String message, String targetRole) {
        this.message = message;
        this.targetRole = targetRole;
        this.isRead = false;
        this.createdAt = LocalDateTime.now();
    }

    /* ================= GETTERS & SETTERS ================= */

    public Long getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public String getTargetRole() {
        return targetRole;
    }

    public boolean isRead() {
        return isRead;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    

     public void setMessage(String message) {
        this.message = message;
    }

    public void setTargetRole(String targetRole) {
        this.targetRole = targetRole;
    }

    public void setRead(boolean read) {
        this.isRead = read;
    }

    public Long getTargetUserId() {
        return targetUserId;
    }

    public void setTargetUserId(Long targetUserId) {
        this.targetUserId = targetUserId;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
