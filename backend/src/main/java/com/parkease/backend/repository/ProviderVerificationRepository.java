package com.parkease.backend.repository;

import com.parkease.backend.entity.ProviderVerification;
import com.parkease.backend.entity.User;
import com.parkease.backend.enumtype.VerificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProviderVerificationRepository
        extends JpaRepository<ProviderVerification, Long> {

    /* ================= BASIC LOOKUPS ================= */

    Optional<ProviderVerification> findByProvider(User provider);

    boolean existsByProvider(com.parkease.backend.entity.User provider);

    /* ================= ADMIN DASHBOARD ================= */

    List<ProviderVerification> findByStatus(VerificationStatus status);

    /* ================= VALIDATION ================= */

    boolean existsByLicenseNumber(String licenseNumber);
}
