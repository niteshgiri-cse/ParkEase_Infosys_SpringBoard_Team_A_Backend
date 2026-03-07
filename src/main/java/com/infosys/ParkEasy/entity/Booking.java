package com.infosys.ParkEasy.entity;

import com.infosys.ParkEasy.entity.type.BookingStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double amount;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    private LocalDateTime bookingTime;

    @ManyToOne
    @JoinColumn(name = "spot_id")
    private ParkingSpot parkingSpot;

}