package com.parkease.backend.controller;

import com.parkease.backend.entity.User;
import com.parkease.backend.repository.UserRepository;
import com.parkease.backend.service.AdminApprovalService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/approvals")
@PreAuthorize("hasRole('ADMIN')")
public class AdminApprovalController {

    private final AdminApprovalService adminApprovalService;
    private final UserRepository userRepository;

    public AdminApprovalController(
            AdminApprovalService adminApprovalService,
            UserRepository userRepository
    ) {
        this.adminApprovalService = adminApprovalService;
        this.userRepository = userRepository;
    }

    /* ================= APPROVE ================= */
    @PutMapping("/provider/{providerId}/approve")
    public ResponseEntity<String> approveProvider(
            Authentication authentication,
            @PathVariable Long providerId,
            @RequestParam(required = false) String remark
    ) {
        Long adminId = getAdminId(authentication);
        adminApprovalService.approveProvider(adminId, providerId, remark);
        return ResponseEntity.ok("Provider approved");
    }

    /* ================= REJECT ================= */
    @PutMapping("/provider/{providerId}/reject")
    public ResponseEntity<String> rejectProvider(
            Authentication authentication,
            @PathVariable Long providerId,
            @RequestParam String remark
    ) {
        Long adminId = getAdminId(authentication);
        adminApprovalService.rejectProvider(adminId, providerId, remark);
        return ResponseEntity.ok("Provider rejected");
    }

    /* ================= SUSPEND ================= */
    @PutMapping("/provider/{providerId}/suspend")
    public ResponseEntity<String> suspendProvider(
            Authentication authentication,
            @PathVariable Long providerId,
            @RequestParam String reason
    ) {
        Long adminId = getAdminId(authentication);
        adminApprovalService.suspendProvider(adminId, providerId, reason);
        return ResponseEntity.ok("Provider suspended");
    }

    /* ================= REACTIVATE ================= */
    @PutMapping("/provider/{providerId}/reactivate")
    public ResponseEntity<String> reactivateProvider(
            Authentication authentication,
            @PathVariable Long providerId
    ) {
        Long adminId = getAdminId(authentication);
        adminApprovalService.reactivateProvider(adminId, providerId);
        return ResponseEntity.ok("Provider reactivated");
    }

    /* ================= HELPER ================= */
    private Long getAdminId(Authentication authentication) {
        String email = authentication.getName();
        User admin = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Admin not found"));
        return admin.getId();
    }
}
