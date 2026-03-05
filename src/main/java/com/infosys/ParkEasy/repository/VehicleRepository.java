package com.infosys.ParkEasy.repository;

import com.infosys.ParkEasy.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleRepository extends JpaRepository<Vehicle,Long> {
}
