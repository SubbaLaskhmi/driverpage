package com.parkease.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

    private String token;   // JWT token
    private String role;    // ADMIN / PROVIDER / DRIVER
    private String message; // optional message
}
