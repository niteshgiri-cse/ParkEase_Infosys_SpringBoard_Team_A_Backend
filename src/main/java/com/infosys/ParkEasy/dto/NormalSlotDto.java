package com.infosys.ParkEasy.dto;

import lombok.Data;

@Data
public class NormalSlotDto {
    private String prefix;
    private Integer total;
    private Integer occupied;
    private Integer evStations;
}