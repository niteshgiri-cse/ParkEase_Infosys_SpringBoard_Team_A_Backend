package com.infosys.ParkEasy.service;

import com.infosys.ParkEasy.dto.ChartResponseDto;
import com.infosys.ParkEasy.dto.DashboardStatsDto;
import com.infosys.ParkEasy.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final ParkingLocationRepository locationRepository;
    private final SlotRepository slotRepository;

    public DashboardStatsDto getDashboardStats() {

        long totalUsers = userRepository.count();
        long totalBookings = bookingRepository.getTotalConfirmedBookings();
        long totalLocations = locationRepository.count();
        long totalSlots = locationRepository.getTotalSlots();
        long bookedSlots = slotRepository.countByBookedTrue();
        long availableSlots = slotRepository.countByBookedFalse();
        double revenue = bookingRepository.getTotalRevenue();
        return new DashboardStatsDto(
                totalLocations,
                totalSlots,
                availableSlots,
                bookedSlots,
                totalUsers,
                totalBookings,
                revenue
        );
    }

    public ChartResponseDto getBookingChart() {
        List<Object[]> result = bookingRepository.getYearlyBookings();
        List<String> years = result.stream()
                .map(r -> String.valueOf(r[0]))
                .toList();
        List<Integer> data = result.stream()
                .map(r -> ((Number) r[1]).intValue())
                .toList();
        ChartResponseDto.Series series =
                new ChartResponseDto.Series("Actual Bookings", data);
        return new ChartResponseDto(List.of(series), years);
    }
}