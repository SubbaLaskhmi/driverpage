package com.parkease.backend.controller;

import com.parkease.backend.dto.ProviderProfileResponse;
import com.parkease.backend.service.ProviderProfileService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/provider/profile")
@CrossOrigin
public class ProviderProfileController {

    private final ProviderProfileService service;

    public ProviderProfileController(ProviderProfileService service) {
        this.service = service;
    }

    @GetMapping
    public ProviderProfileResponse getProfile(Authentication authentication) {
        return service.getProfile(authentication.getName());
    }
}
