package com.parkease.backend.dto.driver;

public class DriverParkingResponse {

    private Long slotId;
    private String parkingName;
    private String location;
    private double pricePerHour;
    private boolean available;
    private String distance;
    private double rating;

    public DriverParkingResponse(
            Long slotId,
            String parkingName,
            String location,
            double pricePerHour,
            boolean available,
            String distance,
            double rating
    ) {
        this.slotId = slotId;
        this.parkingName = parkingName;
        this.location = location;
        this.pricePerHour = pricePerHour;
        this.available = available;
        this.distance = distance;
        this.rating = rating;
    }

    public Long getSlotId() { return slotId; }
    public String getParkingName() { return parkingName; }
    public String getLocation() { return location; }
    public double getPricePerHour() { return pricePerHour; }
    public boolean isAvailable() { return available; }
    public String getDistance() { return distance; }
    public double getRating() { return rating; }
}