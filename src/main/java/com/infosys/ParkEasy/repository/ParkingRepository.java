package com.infosys.ParkEasy.repository;

import com.infosys.ParkEasy.dto.Reponse.ParkingsResponseDto;
import com.infosys.ParkEasy.entity.Parking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ParkingRepository extends JpaRepository<Parking,Long>{

    // TOTAL SLOTS
    @Query("""
    SELECT COUNT(ps)
    FROM ParkingSpot ps
    """)
    Long getTotalSlots();

    // AVAILABLE SLOTS
    @Query("""
    SELECT COUNT(ps)
    FROM ParkingSpot ps
    WHERE ps.status = 'AVAILABLE'
    """)
    Long getAvailableSlots();

    // BOOKED SLOTS
    @Query("""
    SELECT COUNT(ps)
    FROM ParkingSpot ps
    WHERE ps.status = 'OCCUPIED'
    """)
    Long getBookedSlots();
    @Query("""
SELECT new com.infosys.ParkEasy.dto.Reponse.ParkingsResponseDto(
    p.id,
    p.parkingName,
    p.address,
    COUNT(ps.id),

    SUM(CASE
        WHEN ps.id NOT IN (
            SELECT b.parkingSpot.id
            FROM Booking b
            WHERE b.startTime <= CURRENT_TIMESTAMP
            AND b.endTime >= CURRENT_TIMESTAMP
        )
    THEN 1 ELSE 0 END),

    SUM(CASE WHEN ps.slotType = 'EV' THEN 1 ELSE 0 END),

    SUM(CASE
        WHEN ps.slotType = 'EV'
        AND ps.id NOT IN (
            SELECT b.parkingSpot.id
            FROM Booking b
            WHERE b.startTime <= CURRENT_TIMESTAMP
            AND b.endTime >= CURRENT_TIMESTAMP
        )
    THEN 1 ELSE 0 END)
)
FROM Parking p
LEFT JOIN p.spots ps
GROUP BY p.id, p.parkingName, p.address
""")
    List<ParkingsResponseDto> getRealtimeParkingStatus();

    @Query("""
SELECT new com.infosys.ParkEasy.dto.Reponse.ParkingsResponseDto(
    p.id,
    p.parkingName,
    p.address,
    COUNT(ps.id),

    SUM(CASE
        WHEN ps.id NOT IN (
            SELECT b.parkingSpot.id
            FROM Booking b
            WHERE b.startTime <= CURRENT_TIMESTAMP
            AND b.endTime >= CURRENT_TIMESTAMP
        )
    THEN 1 ELSE 0 END),

    SUM(CASE WHEN ps.slotType = 'EV' THEN 1 ELSE 0 END),

    SUM(CASE
        WHEN ps.slotType = 'EV'
        AND ps.id NOT IN (
            SELECT b.parkingSpot.id
            FROM Booking b
            WHERE b.startTime <= CURRENT_TIMESTAMP
            AND b.endTime >= CURRENT_TIMESTAMP
        )
    THEN 1 ELSE 0 END)
)
FROM Parking p
LEFT JOIN p.spots ps
WHERE p.id = :parkingId
GROUP BY p.id, p.parkingName, p.address
""")
    ParkingsResponseDto getParkingRealtimeStatus(Long parkingId);

}