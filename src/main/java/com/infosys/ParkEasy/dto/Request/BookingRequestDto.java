package com.infosys.ParkEasy.dto.Request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BookingRequestDto {

    private String name;
    private String phone;
    private String vehicleNumber;
    private Double amount;
    private String parkingId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private boolean evStation;
}