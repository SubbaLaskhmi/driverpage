package com.parkease.backend.controller.driver;

import com.parkease.backend.dto.driver.DriverParkingResponse;
import com.parkease.backend.service.driver.DriverParkingService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/driver/parking")
@PreAuthorize("hasRole('DRIVER')")
@CrossOrigin
public class DriverParkingController {

    private final DriverParkingService service;

    public DriverParkingController(DriverParkingService service) {
        this.service = service;
    }

    @GetMapping("/nearby")
    public List<DriverParkingResponse> getNearbyParking() {
        return service.getNearbyParking();
    }
}