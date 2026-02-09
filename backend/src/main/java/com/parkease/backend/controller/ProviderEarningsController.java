package com.parkease.backend.controller;

import com.parkease.backend.service.EarningsService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/provider")
@CrossOrigin
public class ProviderEarningsController {

    private final EarningsService earningsService;

    public ProviderEarningsController(EarningsService earningsService) {
        this.earningsService = earningsService;
    }

    @GetMapping("/earnings")
    public Map<String, Object> getEarnings(Authentication auth) {
        return earningsService.getProviderEarnings(auth.getName());
    }
}
