package com.parkease.backend.controller;

import com.parkease.backend.entity.User;
import com.parkease.backend.repository.UserRepository;
import com.parkease.backend.service.ProviderVerificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/provider/verification")
@PreAuthorize("hasRole('PROVIDER')")
public class ProviderVerificationController {

    private final ProviderVerificationService verificationService;
    private final UserRepository userRepository;

    public ProviderVerificationController(
            ProviderVerificationService verificationService,
            UserRepository userRepository
    ) {
        this.verificationService = verificationService;
        this.userRepository = userRepository;
    }

    /* =====================================================
       SUBMIT VERIFICATION DETAILS (PROVIDER)
       ===================================================== */
    @PostMapping("/submit")
    public ResponseEntity<String> submitVerification(
            Authentication authentication,
            @RequestParam String businessName,
            @RequestParam String licenseNumber,
            @RequestParam String documentUrl
    ) {

        // 🔐 Get provider from JWT
        String email = authentication.getName();
        User provider = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Provider not found"));

        verificationService.submitVerification(
                provider.getId(),
                businessName,
                licenseNumber,
                documentUrl
        );

        return ResponseEntity.ok(
                "Verification submitted successfully. Await admin review."
        );
    }
}
