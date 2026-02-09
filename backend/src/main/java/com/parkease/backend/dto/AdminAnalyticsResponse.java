package com.parkease.backend.dto;

import java.util.List;
public class AdminAnalyticsResponse {
     public Revenue revenue;
    public List<BookingTrend> bookingTrend;
    public List<TopProvider> topProviders;
    public UserGrowth userGrowth;

    public static class Revenue {
        public long total = 0;
        public long platformFees = 0;
        public long providerEarnings = 0;
        public int growth = 0;
        public long avgDaily = 0;
    }

    public static class BookingTrend {
        public String day;
         public int bookings;
        public long revenue;
    }

    public static class TopProvider {
        public Long id;
        public String name;
        public int activeSinceDays;
    }

    public static class UserGrowth {
        public Group drivers;
        public Group providers;

        public static class Group {
            public long total;
            public long newThisWeek;
        }
    }

    public Occupancy occupancy;

public static class Occupancy {
    public long totalSlots;
    public long occupiedSlots;
    public long availableSlots;
    public int occupancyPercentage;
}

    
}
