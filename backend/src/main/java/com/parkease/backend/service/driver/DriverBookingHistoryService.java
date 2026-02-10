package com.parkease.backend.service.driver;

import com.parkease.backend.dto.driver.DriverBookingHistoryResponse;
import com.parkease.backend.entity.User;
import com.parkease.backend.repository.BookingRepository;
import com.parkease.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DriverBookingHistoryService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;

    public DriverBookingHistoryService(
            BookingRepository bookingRepository,
            UserRepository userRepository
    ) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
    }

    public List<DriverBookingHistoryResponse> getHistory(String email) {

        User driver = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Driver not found"));

        return bookingRepository.findByDriverOrderByCreatedAtDesc(driver)
                .stream()
                .map(b -> new DriverBookingHistoryResponse(
                        b.getId(),
                        b.getParkingLot().getName(),
                        b.getParkingSlot().getSlotNumber(), 
                        b.getStartTime(),
                        b.getEndTime(),
                        b.getStatus().name()
                ))
                .toList();
    }
}