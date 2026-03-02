package com.infosys.ParkEasy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardStatsDto {

    private long totalLocations;
    private long totalSlots;
    private long availableSlots;
    private long bookedSlots;
    private long totalUsers;
    private long totalBookings;
    private double revenue;
}
