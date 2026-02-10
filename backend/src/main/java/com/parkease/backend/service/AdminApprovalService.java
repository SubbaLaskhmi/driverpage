package com.parkease.backend.service;

import com.parkease.backend.entity.ProviderVerification;
import com.parkease.backend.entity.User;
import com.parkease.backend.enumtype.Role;
import com.parkease.backend.enumtype.VerificationStatus;
import com.parkease.backend.repository.ProviderVerificationRepository;
import com.parkease.backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
public class AdminApprovalService {

    private final UserRepository userRepository;
    private final ProviderVerificationRepository verificationRepository;
    private final NotificationService notificationService;
    private final AdminAuditLogService auditLogService;

    public AdminApprovalService(
            UserRepository userRepository,
            ProviderVerificationRepository verificationRepository,
            NotificationService notificationService,
            AdminAuditLogService auditLogService
    ) {
        this.userRepository = userRepository;
        this.verificationRepository = verificationRepository;
        this.notificationService = notificationService;
        this.auditLogService = auditLogService;
    }

    /* =====================================================
       APPROVE PROVIDER
       ===================================================== */
    public void approveProvider(Long adminId, Long providerId, String remark) {

        User provider = getProvider(providerId);
        String providerName = provider.getFullName();

        ProviderVerification verification =
                verificationRepository.findByProvider(provider)
                        .orElseThrow(() ->
                                new RuntimeException("Verification not found"));

        verification.setStatus(VerificationStatus.VERIFIED);
        verification.setAdminRemark(remark);
        verification.setReviewedAt(LocalDateTime.now());

        provider.setApproved(true);
        provider.setEnabled(true);

        userRepository.save(provider);
        verificationRepository.save(verification);

        notificationService.notifyProvider(
                providerId,
                "Your provider account has been APPROVED by admin."
        );

        auditLogService.log(
                adminId,
                providerId,
                providerName,
                "APPROVED",
                remark
        );
    }

    /* =====================================================
       REJECT PROVIDER
       ===================================================== */
    public void rejectProvider(Long adminId, Long providerId, String remark) {

        User provider = getProvider(providerId);
        String providerName = provider.getFullName();

        ProviderVerification verification =
                verificationRepository.findByProvider(provider)
                        .orElseThrow(() ->
                                new RuntimeException("Verification not found"));

        verification.setStatus(VerificationStatus.REJECTED);
        verification.setAdminRemark(remark);
        verification.setReviewedAt(LocalDateTime.now());

        provider.setApproved(false);
        provider.setEnabled(false);

        verificationRepository.save(verification);
        userRepository.save(provider);

        notificationService.notifyProvider(
                providerId,
                "Your provider verification was REJECTED. Reason: " + remark
        );

        auditLogService.log(
                adminId,
                providerId,
                providerName,
                "REJECTED",
                remark
        );
    }

    /* =====================================================
       SUSPEND PROVIDER
       ===================================================== */
    public void suspendProvider(Long adminId, Long providerId, String reason) {

        User provider = getProvider(providerId);
        String providerName = provider.getFullName();

        provider.setEnabled(false);
        userRepository.save(provider);

        notificationService.notifyProvider(
                providerId,
                "Your provider account has been SUSPENDED. Reason: " + reason
        );

        auditLogService.log(
                adminId,
                providerId,
                providerName,
                "SUSPENDED",
                reason
        );
    }

    /* =====================================================
       REACTIVATE PROVIDER
       ===================================================== */
    public void reactivateProvider(Long adminId, Long providerId) {

        User provider = getProvider(providerId);
        String providerName = provider.getFullName();

        provider.setEnabled(true);
        userRepository.save(provider);

        notificationService.notifyProvider(
                providerId,
                "Your provider account has been REACTIVATED by admin."
        );

        auditLogService.log(
                adminId,
                providerId,
                providerName,
                "REACTIVATED",
                null
        );
    }

    /* =====================================================
       HELPER
       ===================================================== */
    private User getProvider(Long providerId) {
        User user = userRepository.findById(providerId)
                .orElseThrow(() -> new RuntimeException("Provider not found"));

        if (user.getRole() != Role.PROVIDER) {
            throw new RuntimeException("User is not a provider");
        }
        return user;
    }
}
