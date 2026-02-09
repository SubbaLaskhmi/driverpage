package com.parkease.backend.controller;

import com.parkease.backend.dto.CreateParkingSlotRequest;
import com.parkease.backend.dto.ParkingSlotResponse;
import com.parkease.backend.service.SpaceManagementService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/provider/slots")
@CrossOrigin
public class SpaceManagementController {

    private final SpaceManagementService service;

    public SpaceManagementController(SpaceManagementService service) {
        this.service = service;
    }

    @GetMapping
    public List<ParkingSlotResponse> getSlots(Authentication auth) {
        return service.getSlots(auth.getName());
    }

    @PostMapping
    public void addSlot(
            Authentication auth,
            @RequestBody CreateParkingSlotRequest req
    ) {
        service.addSlot(auth.getName(), req);
    }

    @PatchMapping("/{id}/toggle")
    public void toggleSlot(
            Authentication auth,
            @PathVariable Long id
    ) {
        service.toggleSlot(auth.getName(), id);
    }

    @DeleteMapping("/{id}")
    public void deleteSlot(
            Authentication auth,
            @PathVariable Long id
    ) {
        service.deleteSlot(auth.getName(), id);
    }
}
