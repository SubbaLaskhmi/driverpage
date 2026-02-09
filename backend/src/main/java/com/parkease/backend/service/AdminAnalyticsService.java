package com.parkease.backend.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.parkease.backend.dto.AdminAnalyticsResponse;
import com.parkease.backend.dto.ParkingDurationResponse;
import com.parkease.backend.entity.Booking;
import com.parkease.backend.enumtype.BookingStatus;
import com.parkease.backend.enumtype.Role;
import com.parkease.backend.repository.BookingRepository;
import com.parkease.backend.repository.PaymentRepository;
import com.parkease.backend.repository.UserRepository;
import com.parkease.backend.repository.ParkingLotRepository;
import com.parkease.backend.repository.ParkingSlotRepository;

@Service
public class AdminAnalyticsService {

    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final PaymentRepository paymentRepository;
    private final ParkingLotRepository parkingLotRepository;
    private final ParkingSlotRepository parkingSlotRepository;

    public AdminAnalyticsService(
            UserRepository userRepository,
            BookingRepository bookingRepository,
            PaymentRepository paymentRepository,
            ParkingLotRepository parkingLotRepository,
            ParkingSlotRepository parkingSlotRepository
    ) {
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
        this.paymentRepository = paymentRepository;
        this.parkingLotRepository = parkingLotRepository;
        this.parkingSlotRepository = parkingSlotRepository;
    }

    /* ================= MAIN ANALYTICS ================= */

    public AdminAnalyticsResponse getAnalytics() {

        AdminAnalyticsResponse res = new AdminAnalyticsResponse();

        /* ===== USER GROWTH ===== */
        LocalDateTime weekAgo = LocalDateTime.now().minusDays(7);

        AdminAnalyticsResponse.UserGrowth growth =
                new AdminAnalyticsResponse.UserGrowth();

        AdminAnalyticsResponse.UserGrowth.Group drivers =
                new AdminAnalyticsResponse.UserGrowth.Group();
        drivers.total = userRepository.countByRole(Role.DRIVER);
        drivers.newThisWeek =
                userRepository.countByRoleAndCreatedAtAfter(Role.DRIVER, weekAgo);

        AdminAnalyticsResponse.UserGrowth.Group providers =
                new AdminAnalyticsResponse.UserGrowth.Group();
        providers.total = userRepository.countByRole(Role.PROVIDER);
        providers.newThisWeek =
                userRepository.countByRoleAndCreatedAtAfter(Role.PROVIDER, weekAgo);

        growth.drivers = drivers;
        growth.providers = providers;
        res.userGrowth = growth;

        /* ===== TOP PROVIDERS ===== */
        res.topProviders = userRepository.findByRoleAndApprovedTrue(Role.PROVIDER)
                .stream()
                .map(p -> {
                    AdminAnalyticsResponse.TopProvider tp =
                            new AdminAnalyticsResponse.TopProvider();
                    tp.id = p.getId();
                    tp.name = p.getFullName();
                    tp.activeSinceDays =
                            (int) ChronoUnit.DAYS.between(
                                    p.getCreatedAt(), LocalDateTime.now()
                            );
                    return tp;
                })
                .toList();

        /* ===== REVENUE (AGGREGATED) ===== */
        AdminAnalyticsResponse.Revenue revenue =
                new AdminAnalyticsResponse.Revenue();

        revenue.total =
                (long) paymentRepository.sumTotalAmountByStatus(
                        com.parkease.backend.enumtype.PaymentStatus.PAID
                );

        revenue.platformFees =
                (long) paymentRepository.sumPlatformFeeByStatus(
                        com.parkease.backend.enumtype.PaymentStatus.PAID
                );

        revenue.providerEarnings =
                (long) paymentRepository.sumProviderEarningByStatus(
                        com.parkease.backend.enumtype.PaymentStatus.PAID
                );

        res.revenue = revenue;

        /* ===== BOOKING TREND (LAST 7 DAYS) ===== */
        res.bookingTrend = new ArrayList<>();

        for (int i = 6; i >= 0; i--) {
            LocalDate day = LocalDate.now().minusDays(i);
            LocalDateTime start = day.atStartOfDay();
            LocalDateTime end = start.plusDays(1);

            AdminAnalyticsResponse.BookingTrend bt =
                    new AdminAnalyticsResponse.BookingTrend();

            bt.day = day.getDayOfWeek().name().substring(0, 3);

            bt.bookings = (int) bookingRepository
                    .countByStatusAndCreatedAtBetween(
                            BookingStatus.COMPLETED, start, end
                    );

            double dailyRevenue =
                    paymentRepository.sumTotalAmountBetween(start, end);

            bt.revenue = (long) dailyRevenue;

            res.bookingTrend.add(bt);
        }

        /* ===== OCCUPANCY ===== */
        AdminAnalyticsResponse.Occupancy occ =
                new AdminAnalyticsResponse.Occupancy();

        occ.totalSlots = parkingLotRepository.sumActiveTotalSlots();
        occ.occupiedSlots = parkingSlotRepository.countByOccupiedTrue();
        occ.availableSlots = parkingSlotRepository.countByOccupiedFalse();

        occ.occupancyPercentage =
                occ.totalSlots > 0
                        ? (int) ((occ.occupiedSlots * 100) / occ.totalSlots)
                        : 0;

        res.occupancy = occ;

        return res;
    }

    /* ================= PARKING DURATION ================= */

    public ParkingDurationResponse getParkingDurationAnalytics() {

        ParkingDurationResponse res = new ParkingDurationResponse();
        List<ParkingDurationResponse.Bucket> buckets = new ArrayList<>();

        buckets.add(bucket("0–30 min"));
        buckets.add(bucket("30–60 min"));
        buckets.add(bucket("1–2 hrs"));
        buckets.add(bucket("2–4 hrs"));
        buckets.add(bucket("4+ hrs"));

        List<Booking> bookings =
                bookingRepository.findByStatusAndEndTimeIsNotNull(
                        BookingStatus.COMPLETED
                );

        for (Booking b : bookings) {
            long minutes = ChronoUnit.MINUTES.between(
                    b.getStartTime(), b.getEndTime()
            );

            if (minutes <= 30) buckets.get(0).count++;
            else if (minutes <= 60) buckets.get(1).count++;
            else if (minutes <= 120) buckets.get(2).count++;
            else if (minutes <= 240) buckets.get(3).count++;
            else buckets.get(4).count++;
        }

        res.buckets = buckets;
        return res;
    }

    private ParkingDurationResponse.Bucket bucket(String label) {
        ParkingDurationResponse.Bucket b =
                new ParkingDurationResponse.Bucket();
        b.label = label;
        b.count = 0;
        return b;
    }
}
