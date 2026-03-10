package com.infosys.ParkEasy.dto.Reponse;

import com.infosys.ParkEasy.entity.type.UserStatusType;
import lombok.Data;

import java.util.List;

@Data
public class AdminUserResponseDto {
    private String customId;
    private String name;
    private String phone;
    private List<VehicleResponseDto> vehicleDetails;
    private List<AddressResponseDto> address;
    private  BookingResponseDto lastBooking;
    private String totalBooking;
    private UserStatusType userStatusType;
}
