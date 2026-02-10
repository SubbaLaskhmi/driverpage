package com.parkease.backend.controller.driver;

import com.parkease.backend.dto.driver.DriverBookingHistoryResponse;
import com.parkease.backend.service.driver.DriverBookingHistoryService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/driver/bookings")
@PreAuthorize("hasRole('DRIVER')")
public class DriverBookingHistoryController {

    private final DriverBookingHistoryService service;

    public DriverBookingHistoryController(
            DriverBookingHistoryService service
    ) {
        this.service = service;
    }

    @GetMapping("/history")
    public List<DriverBookingHistoryResponse> history(
            Authentication auth
    ) {
        return service.getHistory(auth.getName());
    }
}