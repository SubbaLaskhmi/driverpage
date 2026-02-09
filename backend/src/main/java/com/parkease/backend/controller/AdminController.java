package com.parkease.backend.controller;

import com.parkease.backend.entity.User;
import com.parkease.backend.enumtype.Role;
import com.parkease.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    // 🔹 Get all providers waiting for approval
    @GetMapping("/pending-providers")
    public List<User> getPendingProviders() {
        return userRepository.findAll()
                .stream()
                .filter(user ->
                        user.getRole() == Role.PROVIDER &&
                        !user.isApproved()
                )
                .toList();
    }

    // 🔹 Approve a provider
    @PutMapping("/approve-provider/{id}")
    public String approveProvider(@PathVariable Long id) {

        User provider = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Provider not found"));

        if (provider.getRole() != Role.PROVIDER) {
            throw new RuntimeException("User is not a provider");
        }

        provider.setApproved(true);
        userRepository.save(provider);

        return "Provider approved successfully";
    }

    // 🔹 Reject / disable a provider (optional but useful)
    @PutMapping("/reject-provider/{id}")
    public String rejectProvider(@PathVariable Long id) {

        User provider = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Provider not found"));

        if (provider.getRole() != Role.PROVIDER) {
            throw new RuntimeException("User is not a provider");
        }

        provider.setEnabled(false);
        userRepository.save(provider);

        return "Provider rejected and disabled";
    }
}
