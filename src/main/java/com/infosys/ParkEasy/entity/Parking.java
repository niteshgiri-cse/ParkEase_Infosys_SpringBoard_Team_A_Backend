package com.infosys.ParkEasy.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.infosys.ParkEasy.entity.type.ParkingType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Parking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String parkingName;
    private String address;
    private String city;
    private String phone;
    private String pincode;

    private Double price;

    private LocalTime openTime;
    private LocalTime closeTime;

    private Boolean evEnabled;
    private Double evPrice;

    @Enumerated(EnumType.STRING)
    private ParkingType parkingType;

    @OneToMany(mappedBy = "parking", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Floor> floors;

    @OneToOne(mappedBy = "parking", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private NormalSlot normalSlot;

    public void setNormalSlot(NormalSlot slot) {
        this.normalSlot = slot;
        slot.setParking(this);
    }

    public void addFloor(Floor floor) {
        floors.add(floor);
        floor.setParking(this);
    }
}