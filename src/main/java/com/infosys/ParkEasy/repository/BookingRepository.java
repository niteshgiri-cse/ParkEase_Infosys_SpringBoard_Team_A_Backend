package com.infosys.ParkEasy.repository;

import com.infosys.ParkEasy.entity.Booking;
import com.infosys.ParkEasy.entity.ParkingSpot;
import com.infosys.ParkEasy.entity.type.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query(value = """
    SELECT
        (SELECT COUNT(*) FROM parking) AS totalLocations,
        (SELECT COUNT(*) FROM parking_spot) AS totalSlots,
        (SELECT COUNT(*) FROM parking_spot ps
            WHERE ps.id NOT IN (
                SELECT b.parking_spot_id
                FROM booking b
                WHERE b.start_time <= NOW()
                AND b.end_time >= NOW()
            )
        ) AS availableSlots,
        (SELECT COUNT(*)
            FROM booking
            WHERE start_time <= NOW()
            AND end_time >= NOW()
        ) AS bookedSlots,
        (SELECT COUNT(*) FROM user) AS totalUsers,
        (SELECT COUNT(*)
            FROM booking
            WHERE status='CONFIRMED'
        ) AS totalBookings,
        (SELECT COALESCE(SUM(amount),0)
            FROM booking
            WHERE status='CONFIRMED'
        ) AS revenue
""", nativeQuery = true)
    List<Object[]> getDashboardStats();

    @Query(value = """
            SELECT YEAR(booking_time) AS year, COUNT(*) AS total
            FROM booking
            WHERE status = 'CONFIRMED'
            GROUP BY YEAR(booking_time)
            ORDER BY year
            """, nativeQuery = true)
    List<Object[]> getYearlyBookings();

    List<Booking> findByParkingSpotAndStatusAndEndTimeAfterAndStartTimeBefore(
            ParkingSpot spot,
            String status,
            LocalDateTime start,
            LocalDateTime end
    );
}
