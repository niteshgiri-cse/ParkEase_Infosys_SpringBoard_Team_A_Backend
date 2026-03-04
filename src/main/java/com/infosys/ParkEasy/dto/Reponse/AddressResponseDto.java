package com.infosys.ParkEasy.dto.Reponse;

import com.infosys.ParkEasy.entity.type.AddressType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class AddressResponseDto {

    private Long id;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String country;
    private String pinCode;
    private AddressType addressType;
}
