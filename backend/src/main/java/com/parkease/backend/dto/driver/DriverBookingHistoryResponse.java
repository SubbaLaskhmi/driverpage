package com.parkease.backend.dto.driver;

import java.time.LocalDateTime;

public class DriverBookingHistoryResponse {

    private Long bookingId;
    private String parkingName;
    private String slotNumber;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;

    public DriverBookingHistoryResponse(
            Long bookingId,
            String parkingName,
            String slotNumber,
            LocalDateTime startTime,
            LocalDateTime endTime,
            String status
    ) {
        this.bookingId = bookingId;
        this.parkingName = parkingName;
        this.slotNumber = slotNumber;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
    }

    public Long getBookingId() { return bookingId; }
    public String getParkingName() { return parkingName; }
    public String getSlotNumber() { return slotNumber; }
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public String getStatus() { return status; }
}