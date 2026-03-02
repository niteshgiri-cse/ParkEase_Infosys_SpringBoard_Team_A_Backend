package com.infosys.ParkEasy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class ChartResponseDto {

    private List<Series> series;
    private List<String> categories;

    @Data
    @AllArgsConstructor
    public static class Series {
        private String name;
        private List<Integer> data;
    }
}
