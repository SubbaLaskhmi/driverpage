package com.parkease.backend.controller;

import com.parkease.backend.service.OccupancyService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/provider")
@CrossOrigin
public class ProviderOccupancyController {

    private final OccupancyService occupancyService;

    public ProviderOccupancyController(OccupancyService occupancyService) {
        this.occupancyService = occupancyService;
    }

    @GetMapping("/occupancy")
    public Map<String, Object> getOccupancy(Authentication authentication) {
        return occupancyService.getProviderOccupancy(authentication.getName());
    }
}
