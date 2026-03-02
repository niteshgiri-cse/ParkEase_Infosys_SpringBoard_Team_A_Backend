package com.infosys.ParkEasy.repository;

import com.infosys.ParkEasy.entity.Parking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ParkingRepository extends JpaRepository<Parking,Long>{

    @Query("""
SELECT
COALESCE((SELECT SUM(ns.total) FROM NormalSlot ns),0)+
COALESCE((SELECT SUM(f.total) FROM Floor f),0)
""")
    Long getTotalSlots();

    @Query("""
SELECT
COALESCE((SELECT SUM(ns.total-ns.occupied) FROM NormalSlot ns),0)+
COALESCE((SELECT SUM(f.total-f.occupied) FROM Floor f),0)
""")
    Long getAvailableSlots();

    @Query("""
SELECT
COALESCE((SELECT SUM(ns.occupied) FROM NormalSlot ns),0)+
COALESCE((SELECT SUM(f.occupied) FROM Floor f),0)
""")
    Long getBookedSlots();

    @Query("""
SELECT
COALESCE((SELECT SUM(ns.evStations) FROM NormalSlot ns),0)+
COALESCE((SELECT SUM(f.evStations) FROM Floor f),0)
""")
    Long getTotalEvStations();

    @Query("""
SELECT
COALESCE((SELECT SUM(ns.evOccupied) FROM NormalSlot ns),0)+
COALESCE((SELECT SUM(f.evOccupied) FROM Floor f),0)
""")
    Long getOccupiedEvStations();

    @Query("""
SELECT
COALESCE((SELECT SUM(ns.evStations-ns.evOccupied) FROM NormalSlot ns),0)+
COALESCE((SELECT SUM(f.evStations-f.evOccupied) FROM Floor f),0)
""")
    Long getAvailableEvStations();
}