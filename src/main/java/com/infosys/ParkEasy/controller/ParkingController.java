package com.infosys.ParkEasy.controller;

import com.infosys.ParkEasy.dto.Reponse.ParkingsResponseDto;
import com.infosys.ParkEasy.service.Interface.ParkingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/parking")
@RequiredArgsConstructor
public class ParkingController {
    private final ParkingService parkingService;

    @GetMapping
    public ResponseEntity<List<ParkingsResponseDto>> getParkings(){
          return  parkingService.getParkings();
    }

    @GetMapping("/{parkingId}")
    public ResponseEntity<ParkingsResponseDto> getParkingById(@PathVariable Long parkingId){
       return parkingService.getParkingById(parkingId);
    }
}
