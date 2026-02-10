package com.parkease.backend.controller.driver;

import com.parkease.backend.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/driver")
@CrossOrigin
public class DriverProfileController {

    @GetMapping("/profile")
    public DriverProfileResponse getProfile(Authentication authentication) {

        User driver = (User) authentication.getPrincipal();

        return new DriverProfileResponse(
                driver.getFullName(),
                driver.getEmail(),
                driver.getPhoneNumber()
        );
    }

    record DriverProfileResponse(
            String name,
            String email,
            String phone
    ) {}
}