package com.parkease.backend.service.driver;

import com.parkease.backend.dto.driver.DriverProfileResponse;
import com.parkease.backend.entity.User;
import com.parkease.backend.enumtype.Role;
import com.parkease.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class DriverProfileService {

    private final UserRepository userRepository;

    public DriverProfileService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public DriverProfileResponse getProfile(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Driver not found"));

        if (user.getRole() != Role.DRIVER) {
            throw new RuntimeException("Access denied");
        }

        return new DriverProfileResponse(
                user.getFullName(),
                user.getEmail(),
                user.getPhoneNumber()
        );
    }
}