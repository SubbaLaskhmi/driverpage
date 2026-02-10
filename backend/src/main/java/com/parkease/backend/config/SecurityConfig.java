package com.parkease.backend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> {})

            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            .authorizeHttpRequests(auth -> auth

                // 🔓 AUTH
                .requestMatchers("/api/auth/**").permitAll()

                // 🔐 ADMIN
                .requestMatchers("/api/admin/**").hasAuthority("ROLE_ADMIN")

                // 🔐 PROVIDER (LOGIN ALLOWED EVEN IF NOT APPROVED)
                .requestMatchers(
                        "/api/provider/verification/**",   // upload docs
                        "/api/provider/notifications/**",  // notifications
                        "/api/provider/profile/**"         // profile view
                ).hasAuthority("ROLE_PROVIDER")

                // 🚫 PROVIDER FEATURES (approval enforced in SERVICE layer)
                .requestMatchers(
                        "/api/provider/slots/**",
                        "/api/provider/bookings/**"
                ).hasAuthority("ROLE_PROVIDER")

                // 🔐 DRIVER
                .requestMatchers("/api/driver/**").hasAuthority("ROLE_DRIVER")

                .anyRequest().authenticated()
            )

            .addFilterBefore(
                jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }
}
