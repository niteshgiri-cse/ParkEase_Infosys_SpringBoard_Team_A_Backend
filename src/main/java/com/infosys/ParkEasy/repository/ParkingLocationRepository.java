package com.infosys.ParkEasy.repository;

import com.infosys.ParkEasy.model.ParkingLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ParkingLocationRepository
        extends JpaRepository<ParkingLocation, Long> {

    @Query("SELECT COALESCE(SUM(p.totalSlots),0) FROM ParkingLocation p")
    Long getTotalSlots();
}
