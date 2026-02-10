package com.parkease.backend.controller.driver;

import com.parkease.backend.dto.driver.DriverBookingRequest;
import com.parkease.backend.dto.driver.DriverBookingResponse;
import com.parkease.backend.service.driver.DriverBookingService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/driver/bookings")
@PreAuthorize("hasRole('DRIVER')")
@CrossOrigin
public class DriverBookingController {

    private final DriverBookingService service;

    public DriverBookingController(DriverBookingService service) {
        this.service = service;
    }

    @PostMapping
    public DriverBookingResponse book(
            Authentication authentication,
            @RequestBody DriverBookingRequest request
    ) {
        return service.bookSlot(authentication.getName(), request);
    }
}