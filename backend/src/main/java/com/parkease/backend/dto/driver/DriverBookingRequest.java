package com.parkease.backend.dto.driver;

public class DriverBookingRequest {

    private Long slotId;
    private String vehicleNumber;
    private int hours;

    public Long getSlotId() {
        return slotId;
    }
    public void setSlotId(Long slotId) {
        this.slotId = slotId;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }
    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public int getHours() {
        return hours;
    }
    public void setHours(int hours) {
        this.hours = hours;
    }
}