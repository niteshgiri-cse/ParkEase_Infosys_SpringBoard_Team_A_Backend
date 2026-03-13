package com.infosys.ParkEasy.service.Interface;

import com.infosys.ParkEasy.dto.Reponse.*;
import com.infosys.ParkEasy.dto.Request.ParkingRequestDto;
import com.infosys.ParkEasy.entity.Parking;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AdminService {

    DashboardStatsResponseDto getDashboardStats();
     Parking registerParking(ParkingRequestDto requestDto);
    Parking updateParking(Long id,Parking parking);
    void deleteParking(Long id);
    List<ParkingsResponseDto>getAllParkings();
    Parking getParkingById(Long id);
    UserProfileResponseDto getUserDetails(String customId);
    ResponseEntity<List<AdminUserResponseDto>> getAllUserDetails();
    DashboardResponse dashboard();
}