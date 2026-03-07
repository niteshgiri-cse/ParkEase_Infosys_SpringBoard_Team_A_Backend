package com.infosys.ParkEasy.dto.Request;

import com.infosys.ParkEasy.entity.type.ParkingType;
import lombok.Data;

import java.time.LocalTime;
import java.util.List;

@Data
public class ParkingRequestDto {

    private String parkingName;
    private String address;
    private String city;
    private String phone;
    private String pinCode;

    private Double price;
    private LocalTime openTime;
    private LocalTime closeTime;

    private Boolean evEnabled;
    private Double evPrice;

    private ParkingType parkingType;

    private NormalSlotRequestDto normalSlots;

    private List<FloorRequestDto> floors;
}