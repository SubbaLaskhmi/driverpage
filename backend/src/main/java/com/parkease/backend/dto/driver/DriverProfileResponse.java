package com.parkease.backend.dto.driver;

public class DriverProfileResponse {

    private String name;
    private String email;
    private String phone;

    public DriverProfileResponse(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }
}