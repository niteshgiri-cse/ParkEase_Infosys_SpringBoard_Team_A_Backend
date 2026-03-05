package com.infosys.ParkEasy.dto.Request;

import com.infosys.ParkEasy.dto.Reponse.FloorResponseDto;
import com.infosys.ParkEasy.dto.Reponse.NormalSlotResponseDto;
import com.infosys.ParkEasy.entity.type.ParkingType;
import lombok.Data;

import java.util.List;

@Data
public class ParkingRequestDto {

    private String parkingName;
    private String address;
    private String city;
    private String phone;
    private String pincode;

    private Double price;
    private String openTime;
    private String closeTime;

    private Boolean evEnabled;
    private Double evPrice;

    private ParkingType parkingType;

    private NormalSlotResponseDto normalSlots;

    private List<FloorResponseDto> floors;
}
