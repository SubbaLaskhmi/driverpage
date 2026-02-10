package com.parkease.backend.entity;

import com.parkease.backend.enumtype.AdminAction;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "admin_audit_logs")
public class AdminAuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long adminId;

    @Column(nullable = false)
    private Long providerId;



    
    @Column(nullable = false)
    private String providerName; 

     @Column(nullable = false)
    private String action; 
    // APPROVED / REJECTED / SUSPENDED / REACTIVATED


    @Column(length = 500)
    private String reason;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    public AdminAuditLog() {}

    /* ===== GETTERS & SETTERS ===== */

    public Long getId() {
        return id;
    }

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    public Long getProviderId() {
        return providerId;
    }

    public void setProviderId(Long providerId) {
        this.providerId = providerId;
    }

      public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

   public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
