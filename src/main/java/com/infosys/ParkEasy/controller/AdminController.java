package com.infosys.ParkEasy.controller;

import com.infosys.ParkEasy.dto.ChartResponseDto;
import com.infosys.ParkEasy.dto.DashboardStatsDto;
import com.infosys.ParkEasy.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final DashboardService dashboardService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/stats")
    public ResponseEntity<DashboardStatsDto> getDashboardStats() {
        return ResponseEntity.ok(dashboardService.getDashboardStats());
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/bookings")
    public ChartResponseDto getBookingChart() {
        return dashboardService.getBookingChart();
    }
}
