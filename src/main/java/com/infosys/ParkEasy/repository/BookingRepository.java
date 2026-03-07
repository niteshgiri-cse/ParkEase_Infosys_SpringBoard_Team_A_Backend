package com.infosys.ParkEasy.repository;

import com.infosys.ParkEasy.entity.Booking;
import com.infosys.ParkEasy.entity.type.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    long countByStatus(String status);

    @Query("SELECT COALESCE(SUM(b.amount),0) FROM Booking b WHERE b.status='BOOKED'")
    Double getTotalRevenue();
    @Query("SELECT COUNT(b) FROM Booking b WHERE b.status = 'CONFIRMED'")
    long getTotalConfirmedBookings();

    // Year-wise confirmed bookings
    @Query(value = """
            SELECT YEAR(booking_time) AS year, COUNT(*) AS total
            FROM booking
            WHERE status = 'CONFIRMED'
            GROUP BY YEAR(booking_time)
            ORDER BY year
            """, nativeQuery = true)
    List<Object[]> getYearlyBookings();
}
