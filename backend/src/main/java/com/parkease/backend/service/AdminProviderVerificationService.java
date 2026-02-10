package com.parkease.backend.service;

import com.parkease.backend.dto.ProviderVerificationResponse;
import com.parkease.backend.entity.ProviderVerification;
import com.parkease.backend.entity.User;
import com.parkease.backend.enumtype.VerificationStatus;
import com.parkease.backend.repository.ProviderVerificationRepository;
import com.parkease.backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class AdminProviderVerificationService {

    private final ProviderVerificationRepository verificationRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    public AdminProviderVerificationService(
            ProviderVerificationRepository verificationRepository,
            UserRepository userRepository,
            NotificationService notificationService
    ) {
        this.verificationRepository = verificationRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
    }

    /* =====================================================
       GET ALL PENDING VERIFICATIONS
       ===================================================== */
    public List<ProviderVerificationResponse> getPendingVerifications() {
        return verificationRepository
                .findByStatus(VerificationStatus.PENDING)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    /* =====================================================
       APPROVE VERIFICATION
       ===================================================== */
    public void approve(Long verificationId, String remark) {

        ProviderVerification verification = verificationRepository
                .findById(verificationId)
                .orElseThrow(() -> new RuntimeException("Verification not found"));

        verification.setStatus(VerificationStatus.VERIFIED);
        verification.setAdminRemark(remark);
        verification.setReviewedAt(LocalDateTime.now());

        User provider = verification.getProvider();
        provider.setApproved(true);
        provider.setEnabled(true);

        userRepository.save(provider);
        verificationRepository.save(verification);

        notificationService.notifyProvider(
                provider.getId(),
                "Your provider verification has been APPROVED by admin."
        );
    }

    /* =====================================================
       REJECT VERIFICATION
       ===================================================== */
    public void reject(Long verificationId, String remark) {

        ProviderVerification verification = verificationRepository
                .findById(verificationId)
                .orElseThrow(() -> new RuntimeException("Verification not found"));

        verification.setStatus(VerificationStatus.REJECTED);
        verification.setAdminRemark(remark);
        verification.setReviewedAt(LocalDateTime.now());

        User provider = verification.getProvider();
        provider.setApproved(false);
        provider.setEnabled(false);

        userRepository.save(provider);
        verificationRepository.save(verification);

        notificationService.notifyProvider(
                provider.getId(),
                "Your provider verification was REJECTED. Reason: " + remark
        );
    }

    /* =====================================================
       MAPPER
       ===================================================== */
    private ProviderVerificationResponse mapToResponse(ProviderVerification v) {

        ProviderVerificationResponse dto = new ProviderVerificationResponse();

        dto.setVerificationId(v.getId());
        dto.setProviderId(v.getProvider().getId());
        dto.setProviderName(v.getProvider().getFullName());
        dto.setEmail(v.getProvider().getEmail());
        dto.setBusinessName(v.getBusinessName());
        dto.setLicenseNumber(v.getLicenseNumber());
        dto.setDocumentUrl(v.getDocumentUrl());
        dto.setSubmittedAt(v.getCreatedAt());
        dto.setStatus(v.getStatus().name());

        return dto;
    }
}
