package com.infosys.ParkEasy.dto.Reponse;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.infosys.ParkEasy.entity.Address;
import com.infosys.ParkEasy.entity.Vehicle;
import com.infosys.ParkEasy.entity.type.RoleType;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Data
public class UserProfileResponseDto {
    private String customId;
    private String name;
    private String email;
    private String phone;
    private List<RoleType> roles = new ArrayList<>();
    private List<Address> addresses = new ArrayList<>();
    private List<Vehicle> vehicles = new ArrayList<>();

}
