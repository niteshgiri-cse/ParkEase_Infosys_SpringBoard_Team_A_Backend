package com.infosys.ParkEasy.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NormalSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String prefix;
    private Integer total;
    private Integer occupied;
    private Integer evStations;
    private Integer evOccupied;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parking_id")
    @JsonBackReference
    private Parking parking;
}