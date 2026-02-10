package com.parkease.backend.service;

import com.parkease.backend.dto.ProviderVerificationResponse;
import com.parkease.backend.entity.ProviderVerification;
import com.parkease.backend.entity.User;
import com.parkease.backend.enumtype.Role;
import com.parkease.backend.enumtype.VerificationStatus;
import com.parkease.backend.repository.ProviderVerificationRepository;
import com.parkease.backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
public class ProviderVerificationService {

    private final ProviderVerificationRepository verificationRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    public ProviderVerificationService(
            ProviderVerificationRepository verificationRepository,
            UserRepository userRepository,
            NotificationService notificationService
    ) {
        this.verificationRepository = verificationRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
    }

    /* =====================================================
       SUBMIT VERIFICATION (PROVIDER)
       ===================================================== */
    public void submitVerification(
            Long providerId,
            String businessName,
            String licenseNumber,
            String documentUrl
    ) {

        User provider = userRepository.findById(providerId)
                .orElseThrow(() -> new RuntimeException("Provider not found"));

        // 🔒 Role validation
        if (provider.getRole() != Role.PROVIDER) {
            throw new RuntimeException("Only providers can submit verification");
        }

        // 🚫 Prevent duplicate submissions
        if (verificationRepository.existsByProvider(provider)) {
            throw new RuntimeException("Verification already submitted");
        }

        ProviderVerification verification = new ProviderVerification();
        verification.setProvider(provider);
        verification.setBusinessName(businessName);
        verification.setLicenseNumber(licenseNumber);
        verification.setDocumentUrl(documentUrl);
        verification.setStatus(VerificationStatus.PENDING);
        verification.setCreatedAt(LocalDateTime.now());

        verificationRepository.save(verification);

        // 🔔 Notify ADMIN
        notificationService.notifyAdmin(
                "New provider verification submitted by " + provider.getFullName()
        );
    }

    /* =====================================================
       FETCH VERIFICATION BY PROVIDER
       ===================================================== */
    public ProviderVerification getByProvider(User provider) {
        return verificationRepository.findByProvider(provider)
                .orElseThrow(() -> new RuntimeException("Verification not found"));
    }

    /* =====================================================
       MARK VERIFIED (ADMIN)
       ===================================================== */
    public void markVerified(ProviderVerification verification, String remark) {

        verification.setStatus(VerificationStatus.VERIFIED);
        verification.setAdminRemark(remark);
        verification.setReviewedAt(LocalDateTime.now());

        verificationRepository.save(verification);

        // 🔔 Notify PROVIDER ✅ FIXED
        notificationService.notifyProvider(
                verification.getProvider().getId(),
                "Your provider verification has been approved by admin."
        );
    }

    /* =====================================================
       MARK REJECTED (ADMIN)
       ===================================================== */
    public void markRejected(ProviderVerification verification, String remark) {

        verification.setStatus(VerificationStatus.REJECTED);
        verification.setAdminRemark(remark);
        verification.setReviewedAt(LocalDateTime.now());

        verificationRepository.save(verification);

        // 🔔 Notify PROVIDER ✅ FIXED
        notificationService.notifyProvider(
                verification.getProvider().getId(),
                "Your provider verification was rejected by admin. Reason: " + remark
        );
    }



    /* =====================================================
   FETCH ALL PENDING VERIFICATIONS (ADMIN)
   ===================================================== */
public List<ProviderVerificationResponse> getPendingVerifications() {

    return verificationRepository
            .findByStatus(VerificationStatus.PENDING)
            .stream()
            .map(v -> {
                ProviderVerificationResponse dto =
                        new ProviderVerificationResponse();

                dto.setVerificationId(v.getId());
                dto.setProviderId(v.getProvider().getId());
                dto.setProviderName(v.getProvider().getFullName());
                dto.setEmail(v.getProvider().getEmail());
                dto.setBusinessName(v.getBusinessName());
                dto.setLicenseNumber(v.getLicenseNumber());
                dto.setDocumentUrl(v.getDocumentUrl());
                dto.setSubmittedAt(v.getCreatedAt());

                return dto;
            })
            .collect(Collectors.toList());
}

}
