package com.infosys.ParkEasy.service.Interface;

import com.infosys.ParkEasy.dto.Reponse.ParkingsResponseDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ParkingService {
    ResponseEntity<List<ParkingsResponseDto>> getParkings();

    ResponseEntity<ParkingsResponseDto> getParkingById(Long parkingId);
}
