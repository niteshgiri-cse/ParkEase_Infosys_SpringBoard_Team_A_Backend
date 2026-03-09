package com.infosys.ParkEasy.service.imp;

import com.infosys.ParkEasy.dto.Reponse.ParkingsResponseDto;
import com.infosys.ParkEasy.repository.ParkingRepository;
import com.infosys.ParkEasy.service.Interface.ParkingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ParkingServiceImp implements ParkingService {

    private final ParkingRepository parkingRepository;

    @Override
    public ResponseEntity<List<ParkingsResponseDto>> getParkings() {

        List<ParkingsResponseDto> parkings =
                parkingRepository.getRealtimeParkingStatus();

        return ResponseEntity.ok(parkings);
    }

    @Override
    public ResponseEntity<ParkingsResponseDto> getParkingById(Long parkingId) {

        ParkingsResponseDto parking =
                parkingRepository.getParkingRealtimeStatus(parkingId);

        return ResponseEntity.ok(parking);
    }
}