package com.parkease.backend.controller;

import com.parkease.backend.dto.*;
import com.parkease.backend.enumtype.Role;
import com.parkease.backend.service.AuthService;
import com.parkease.backend.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private NotificationService notificationService;

    // ================= REGISTER =================
    @PostMapping("/register")
    public AuthResponse register(@RequestBody RegisterRequest request) {

        AuthResponse response = authService.register(request);

        /* =====================================================
           🔔 NOTIFY ADMIN ON PROVIDER REGISTRATION
           ===================================================== */
        if (request.getRole() == Role.PROVIDER) {
            notificationService.notifyAdmin(
                    "New provider registration: " + request.getFullName()
            );
        }

        return response;
    }

    // ================= LOGIN =================
    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

    // ================= FORGOT PASSWORD =================
    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestBody ForgotPasswordRequest request) {
        authService.sendResetOtp(request.getEmail());
        return "OTP sent to registered email";
    }

    // ================= RESET PASSWORD =================
    @PostMapping("/reset-password")
    public String resetPassword(@RequestBody ResetPasswordRequest request) {
        authService.resetPassword(
                request.getEmail(),
                request.getOtp(),
                request.getNewPassword()
        );
        return "Password reset successful";
    }
}
