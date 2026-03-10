package com.infosys.ParkEasy.dto.Reponse;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ParkingsResponseDto {
    private Long id;
    private String parkingName;
    private String parkingAddress;
    private Long totalSlot;
    private Long availableSlot;
    private Long evStation;
    private Long evAvailable;
}