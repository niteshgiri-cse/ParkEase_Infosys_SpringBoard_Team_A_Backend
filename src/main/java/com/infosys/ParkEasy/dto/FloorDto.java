package com.infosys.ParkEasy.dto;

import lombok.Data;

@Data
public class FloorDto {
    private String floorName;
    private String prefix;
    private Integer total;
    private Integer occupied;
    private Integer evStations;
}
