package com.parkease.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.parkease.backend.dto.AdminProviderResponse;
import com.parkease.backend.service.AdminProviderService;

@RestController
@RequestMapping("/api/admin/providers")
@PreAuthorize("hasRole('ADMIN')")
public class AdminProviderController {

    private final AdminProviderService service;

    public AdminProviderController(AdminProviderService service) {
        this.service = service;
    }

    /* =====================================================
       GET ALL PROVIDERS (ADMIN APPROVAL LIST)
       ===================================================== */
    @GetMapping
    public ResponseEntity<List<AdminProviderResponse>> getProviders() {
        return ResponseEntity.ok(service.getAllProviders());
    }

    /* =====================================================
       APPROVE PROVIDER
       ===================================================== */
    @PutMapping("/{id}/approve")
    public ResponseEntity<Void> approve(@PathVariable Long id) {
        service.approveProvider(id);
        return ResponseEntity.ok().build();
    }

    /* =====================================================
       SUSPEND PROVIDER
       ===================================================== */
    @PutMapping("/{id}/suspend")
    public ResponseEntity<Void> suspend(@PathVariable Long id) {
        service.suspendProvider(id);
        return ResponseEntity.ok().build();
    }

    /* =====================================================
       REACTIVATE PROVIDER
       ===================================================== */
    @PutMapping("/{id}/reactivate")
    public ResponseEntity<Void> reactivate(@PathVariable Long id) {
        service.reactivateProvider(id);
        return ResponseEntity.ok().build();
    }

    /* =====================================================
       REJECT PROVIDER (DELETE)
       ===================================================== */
    @DeleteMapping("/{id}/reject")
    public ResponseEntity<Void> reject(@PathVariable Long id) {
        service.rejectProvider(id);
        return ResponseEntity.ok().build();
    }
}
