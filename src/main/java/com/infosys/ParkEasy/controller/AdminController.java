package com.infosys.ParkEasy.controller;

import com.infosys.ParkEasy.dto.Reponse.ChartResponseDto;
import com.infosys.ParkEasy.dto.Reponse.DashboardStatsResponseDto;
import com.infosys.ParkEasy.dto.Reponse.UserProfileResponseDto;
import com.infosys.ParkEasy.entity.Parking;

import com.infosys.ParkEasy.service.Interface.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/stats")
    public ResponseEntity<DashboardStatsResponseDto> getDashboardStats() {
        return ResponseEntity.ok(adminService.getDashboardStats());
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/bookings")
    public ChartResponseDto getBookingChart() {
        return adminService.getBookingChart();
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/parking")
    public Parking createParking(@RequestBody Parking parking){
        return adminService.createParking(parking);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/parking/{id}")
    public Parking updateParking(@PathVariable Long id,@RequestBody Parking parking){
        return adminService.updateParking(id,parking);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/parking/{id}")
    public void deleteParking(@PathVariable Long id){
        adminService.deleteParking(id);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/parking")
    public List<Parking> getAll(){
        return adminService.getAllParkings();
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/parking/{id}")
    public Parking getById(@PathVariable Long id){
        return adminService.getParkingById(id);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/total-slots")
    public Long totalSlots(){
        return adminService.totalSlots();
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/dashboard/available-slots")
    public Long availableSlots(){
        return adminService.availableSlots();
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/dashboard/booked-slots")
    public Long bookedSlots(){
        return adminService.bookedSlots();
    }

    @GetMapping("/user/{customId}")
    ResponseEntity<UserProfileResponseDto> getUserDetails(@PathVariable String customId){
     return   ResponseEntity.ok(adminService.getUserDetails(customId));
    }


}
