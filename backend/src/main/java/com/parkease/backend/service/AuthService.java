package com.parkease.backend.service;

import com.parkease.backend.config.JwtService;
import com.parkease.backend.dto.AuthResponse;
import com.parkease.backend.dto.LoginRequest;
import com.parkease.backend.dto.RegisterRequest;
import com.parkease.backend.entity.Notification;
import com.parkease.backend.entity.User;
import com.parkease.backend.entity.VerificationToken;
import com.parkease.backend.enumtype.Role;
import com.parkease.backend.repository.NotificationRepository;
import com.parkease.backend.repository.UserRepository;
import com.parkease.backend.repository.VerificationTokenRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private NotificationRepository notificationRepository; // 🔔 ADDED

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private EmailService emailService;

    /* =====================================================
       REGISTER
       ===================================================== */
    public AuthResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        if (userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new RuntimeException("Phone number already registered");
        }

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new RuntimeException("Passwords do not match");
        }

        /* 🔒 Allow only ONE admin */
        if (request.getRole() == Role.ADMIN) {
            long adminCount = userRepository.countByRole(Role.ADMIN);
            if (adminCount >= 1) {
                throw new RuntimeException("Admin already exists");
            }
        }

        /* 🅿️ Provider mandatory details */
        if (request.getRole() == Role.PROVIDER) {
            if (request.getParkingAreaName() == null ||
                request.getLocation() == null) {
                throw new RuntimeException("Provider details are required");
            }
        }

        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());

        /* 🔑 Approval logic */
        if (request.getRole() == Role.PROVIDER) {
            user.setApproved(false);   // Admin approval required
            user.setEnabled(true);
        } else {
            user.setApproved(true);    // ADMIN & DRIVER auto-approved
            user.setEnabled(true);
        }

        userRepository.save(user);

        /* 🔔 NOTIFY ADMIN ON REGISTRATION */
        if (request.getRole() == Role.PROVIDER || request.getRole() == Role.DRIVER) {
            Notification notification = new Notification();
            notification.setMessage(
                    "New " + request.getRole().name().toLowerCase()
                    + " registered: " + user.getFullName()
            );
            notification.setTargetRole("ADMIN");
            notification.setRead(false);
            notification.setCreatedAt(LocalDateTime.now());

            notificationRepository.save(notification);
        }

        /* 📩 RESPONSE */
        if (request.getRole() == Role.PROVIDER) {
            return AuthResponse.builder()
                    .message("Registered successfully. Await admin approval.")
                    .build();
        }

        return AuthResponse.builder()
                .message("Registered successfully. You can login now.")
                .build();
    }

    /* =====================================================
       LOGIN
       ===================================================== */
    public AuthResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        /* =====================================================
           PROVIDER APPROVAL ENFORCEMENT
           ===================================================== */
        if (user.getRole() == Role.PROVIDER) {

            if (!user.isApproved()) {
                throw new RuntimeException(
                        "Your provider account is awaiting admin approval."
                );
            }

            if (!user.isEnabled()) {
                throw new RuntimeException(
                        "Your provider account has been suspended by admin."
                );
            }
        }

        /* =====================================================
           ACCOUNT ENABLE CHECK
           ===================================================== */
        if (!user.isEnabled()) {
            throw new RuntimeException("Your account is disabled");
        }

        /* =====================================================
           GENERATE TOKEN
           ===================================================== */
        String token = jwtService.generateToken(user);

        return AuthResponse.builder()
                .token(token)
                .role(user.getRole().name())
                .build();
    }

    /* =====================================================
       FORGOT PASSWORD
       ===================================================== */
    public void sendResetOtp(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found with this email"));

        String otp = String.valueOf(
                100000 + new Random().nextInt(900000));

        verificationTokenRepository.deleteByUser(user);

        VerificationToken token = new VerificationToken(
                otp,
                LocalDateTime.now().plusMinutes(10),
                user
        );

        verificationTokenRepository.save(token);
        emailService.sendOtpEmail(user.getEmail(), otp);
    }

    /* =====================================================
       RESET PASSWORD
       ===================================================== */
    public void resetPassword(String email, String otp, String newPassword) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        VerificationToken token =
                verificationTokenRepository.findByToken(otp)
                        .orElseThrow(() ->
                                new RuntimeException("Invalid OTP"));

        if (!token.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("OTP does not belong to this user");
        }

        if (token.isExpired()) {
            throw new RuntimeException("OTP has expired");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        verificationTokenRepository.deleteByUser(user);
    }
}
