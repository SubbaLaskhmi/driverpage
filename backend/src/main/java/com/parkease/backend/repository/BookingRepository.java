package com.parkease.backend.repository;

import com.parkease.backend.entity.Booking;
import com.parkease.backend.entity.ParkingLot;
import com.parkease.backend.entity.User;
import com.parkease.backend.enumtype.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByDriver(User driver);

    List<Booking> findByDriverOrderByCreatedAtDesc(User driver);

    List<Booking> findByParkingLot(ParkingLot parkingLot);

    List<Booking> findByStatus(BookingStatus status);
     
@Query("""
    SELECT b
    FROM Booking b
    WHERE b.parkingLot.provider.id = :providerId
      AND b.status = 'COMPLETED'
""")
List<Booking> findTodayBookingsForProvider(@Param("providerId") Long providerId);




    @Query("""
    SELECT b
    FROM Booking b
    WHERE b.parkingLot.provider.id = :providerId
      AND b.status = 'COMPLETED'
""")
List<Booking> findCompletedBookingsForProvider(@Param("providerId") Long providerId);





    @Query("""
        SELECT b
        FROM Booking b
        WHERE b.parkingLot.id = :parkingLotId
          AND b.status IN :statuses
    """)
    List<Booking> findActiveBookingsForLot(
            @Param("parkingLotId") Long parkingLotId,
            @Param("statuses") List<BookingStatus> statuses
    );

    long countByStatus(BookingStatus status);

    long countByCreatedAtAfter(LocalDateTime date);
    long countByStatusAndCreatedAtBetween(
    BookingStatus status,
    LocalDateTime start,
    LocalDateTime end
);

@Query("""
    SELECT b
    FROM Booking b
    WHERE b.parkingLot.id = :lotId
      AND b.status IN ('COMPLETED', 'CANCELLED')
    ORDER BY b.endTime DESC
""")
List<Booking> findBookingHistory(@Param("lotId") Long lotId);




@Query("""
    SELECT b
    FROM Booking b
    WHERE b.parkingLot.provider.id = :providerId
      AND b.status = 'COMPLETED'
      AND b.endTime >= :start
""")
List<Booking> findCompletedBookingsAfter(
        @Param("providerId") Long providerId,
        @Param("start") java.time.LocalDateTime start
);

List<Booking> findByStatusAndEndTimeIsNotNull(BookingStatus status);


}