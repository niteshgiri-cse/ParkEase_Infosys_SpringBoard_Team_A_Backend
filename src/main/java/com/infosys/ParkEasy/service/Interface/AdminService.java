package com.infosys.ParkEasy.service.Interface;

import com.infosys.ParkEasy.dto.ChartResponseDto;
import com.infosys.ParkEasy.dto.DashboardStatsDto;
import com.infosys.ParkEasy.entity.Parking;

import java.util.List;

public interface AdminService {

    DashboardStatsDto getDashboardStats();
    ChartResponseDto getBookingChart();
    Parking createParking(Parking parking);
    Parking updateParking(Long id,Parking parking);
    void deleteParking(Long id);
    List<Parking> getAllParkings();
    Parking getParkingById(Long id);
    Long totalSlots();
    Long availableSlots();
    Long bookedSlots();
}