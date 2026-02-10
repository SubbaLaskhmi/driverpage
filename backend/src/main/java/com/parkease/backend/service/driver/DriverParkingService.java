package com.parkease.backend.service.driver;

import com.parkease.backend.dto.driver.DriverParkingResponse;
import com.parkease.backend.entity.ParkingLot;
import com.parkease.backend.entity.ParkingSlot;
import com.parkease.backend.repository.ParkingLotRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DriverParkingService {

    private final ParkingLotRepository parkingLotRepository;

    public DriverParkingService(ParkingLotRepository parkingLotRepository) {
        this.parkingLotRepository = parkingLotRepository;
    }

    public List<DriverParkingResponse> getNearbyParking() {

        List<ParkingLot> lots = parkingLotRepository.findAll();
        List<DriverParkingResponse> response = new ArrayList<>();

        for (ParkingLot lot : lots) {

            // 🔹 Build readable location string
            String location =
                    lot.getAddress()
                    + (lot.getCity() != null ? ", " + lot.getCity() : "")
                    + (lot.getState() != null ? ", " + lot.getState() : "");

            for (ParkingSlot slot : lot.getSlots()) {

                boolean available =
                        slot.isActive() && !slot.isOccupied();

                response.add(
                    new DriverParkingResponse(
                        slot.getId(),
                        lot.getName(),
                        location,
                        50.0,        // TEMP price (explained below)
                        available,
                        "0.5 km",    // dummy distance
                        4.5          // dummy rating
                    )
                );
            }
        }

        return response;
    }
}