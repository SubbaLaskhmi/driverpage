package com.parkease.backend.repository;

import com.parkease.backend.entity.ParkingLot;
import com.parkease.backend.entity.Payment;
import com.parkease.backend.enumtype.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    // ---------- BASIC QUERIES ----------
    List<Payment> findByStatus(PaymentStatus status);

    long countByStatus(PaymentStatus status);


    // ---------- SUM QUERIES (FIXED) ----------
    @Query("""
        SELECT COALESCE(SUM(p.totalAmount), 0)
        FROM Payment p
        WHERE p.status = :status
    """)
    double sumTotalAmountByStatus(@Param("status") PaymentStatus status);


    @Query("""
        SELECT COALESCE(SUM(p.platformFee), 0)
        FROM Payment p
        WHERE p.status = :status
    """)
    double sumPlatformFeeByStatus(@Param("status") PaymentStatus status);


    @Query("""
        SELECT COALESCE(SUM(p.providerEarning), 0)
        FROM Payment p
        WHERE p.status = :status
    """)
    double sumProviderEarningByStatus(@Param("status") PaymentStatus status);


    // ---------- DATE BASED ----------
    @Query("""
        SELECT COALESCE(SUM(p.totalAmount), 0)
        FROM Payment p
        WHERE p.paidAt BETWEEN :start AND :end
    """)
    double sumTotalAmountBetween(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    List<Payment> findByPaidAtAfter(LocalDateTime date);
    
   
}
