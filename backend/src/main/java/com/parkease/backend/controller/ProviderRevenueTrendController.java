package com.parkease.backend.controller;

import com.parkease.backend.service.RevenueTrendService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/provider")
@CrossOrigin
public class ProviderRevenueTrendController {

    private final RevenueTrendService revenueTrendService;

    public ProviderRevenueTrendController(RevenueTrendService revenueTrendService) {
        this.revenueTrendService = revenueTrendService;
    }

    @GetMapping("/revenue-trend")
    public Map<String, Object> getRevenueTrend(Authentication auth) {
        return revenueTrendService.getRevenueTrend(auth.getName());
    }
}
