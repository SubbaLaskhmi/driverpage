package com.parkease.backend.service.driver;

import com.parkease.backend.dto.driver.DriverBookingRequest;
import com.parkease.backend.dto.driver.DriverBookingResponse;
import com.parkease.backend.entity.*;
import com.parkease.backend.enumtype.BookingStatus;
import com.parkease.backend.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class DriverBookingService {

    private final UserRepository userRepository;
    private final ParkingSlotRepository parkingSlotRepository;
    private final BookingRepository bookingRepository;

    public DriverBookingService(
            UserRepository userRepository,
            ParkingSlotRepository parkingSlotRepository,
            BookingRepository bookingRepository
    ) {
        this.userRepository = userRepository;
        this.parkingSlotRepository = parkingSlotRepository;
        this.bookingRepository = bookingRepository;
    }

    public DriverBookingResponse bookSlot(
            String driverEmail,
            DriverBookingRequest request
    ) {

        User driver = userRepository.findByEmail(driverEmail)
                .orElseThrow(() -> new RuntimeException("Driver not found"));

        ParkingSlot slot = parkingSlotRepository.findById(request.getSlotId())
                .orElseThrow(() -> new RuntimeException("Slot not found"));

        if (slot.isOccupied()) {
            throw new RuntimeException("Slot already occupied");
        }

        // ⏱ Time calculation
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = startTime.plusHours(request.getHours());

        // 🧾 Create booking
        Booking booking = new Booking();
        booking.setDriver(driver);
        booking.setParkingLot(slot.getParkingLot());
        booking.setParkingSlot(slot);
        booking.setVehicleNumber(request.getVehicleNumber());
        booking.setStartTime(startTime);
        booking.setEndTime(endTime);
        booking.setStatus(BookingStatus.ACTIVE);

        bookingRepository.save(booking);

        // 🚫 Mark slot as occupied
        slot.setOccupied(true);
        parkingSlotRepository.save(slot);

        return new DriverBookingResponse(
                booking.getId(),
                slot.getParkingLot().getName(),
                slot.getSlotNumber(),
                startTime,
                endTime,
                booking.getStatus().name()
        );
    }
}