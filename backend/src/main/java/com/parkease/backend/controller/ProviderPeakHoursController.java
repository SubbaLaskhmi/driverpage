package com.parkease.backend.controller;

import com.parkease.backend.service.PeakHoursService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/provider")
@CrossOrigin
public class ProviderPeakHoursController {

    private final PeakHoursService peakHoursService;

    public ProviderPeakHoursController(PeakHoursService peakHoursService) {
        this.peakHoursService = peakHoursService;
    }

    @GetMapping("/peak-hours")
    public List<Map<String, Object>> getPeakHours(Authentication auth) {
        return peakHoursService.getPeakHours(auth.getName());
    }
}
