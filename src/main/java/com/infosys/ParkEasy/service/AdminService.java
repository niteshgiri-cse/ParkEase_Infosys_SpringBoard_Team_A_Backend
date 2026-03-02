package com.infosys.ParkEasy.service;

import com.infosys.ParkEasy.dto.ChartResponseDto;
import com.infosys.ParkEasy.dto.DashboardStatsDto;
import com.infosys.ParkEasy.entity.Floor;
import com.infosys.ParkEasy.entity.NormalSlot;
import com.infosys.ParkEasy.entity.Parking;
import com.infosys.ParkEasy.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final ParkingRepository parkingRepository;
    private final SlotRepository slotRepository;

    public DashboardStatsDto getDashboardStats() {

        long totalUsers = userRepository.count();
        long totalBookings = bookingRepository.getTotalConfirmedBookings();
        long totalLocations = parkingRepository.count();
        long totalSlots = parkingRepository.getTotalSlots();
        long bookedSlots = slotRepository.countByBookedTrue();
        long availableSlots = slotRepository.countByBookedFalse();
        double revenue = bookingRepository.getTotalRevenue();
        return new DashboardStatsDto(
                totalLocations,
                totalSlots,
                availableSlots,
                bookedSlots,
                totalUsers,
                totalBookings,
                revenue
        );
    }

    public ChartResponseDto getBookingChart() {
        List<Object[]> result = bookingRepository.getYearlyBookings();
        List<String> years = result.stream()
                .map(r -> String.valueOf(r[0]))
                .toList();
        List<Integer> data = result.stream()
                .map(r -> ((Number) r[1]).intValue())
                .toList();
        ChartResponseDto.Series series =
                new ChartResponseDto.Series("Actual Bookings", data);
        return new ChartResponseDto(List.of(series), years);
    }

    public Parking createParking(Parking parking){
        if(parking.getNormalSlot()!=null){
            parking.getNormalSlot().setParking(parking);
        }
        if(parking.getFloors()!=null){
            parking.getFloors().forEach(f->f.setParking(parking));
        }
        return parkingRepository.save(parking);
    }

    public Parking updateParking(Long id,Parking parking){
        Parking existing=parkingRepository.findById(id).orElseThrow();
        existing.setParkingName(parking.getParkingName());
        existing.setAddress(parking.getAddress());
        existing.setCity(parking.getCity());
        existing.setPhone(parking.getPhone());
        existing.setPincode(parking.getPincode());
        existing.setPrice(parking.getPrice());
        existing.setOpenTime(parking.getOpenTime());
        existing.setCloseTime(parking.getCloseTime());
        existing.setEvEnabled(parking.getEvEnabled());
        existing.setEvPrice(parking.getEvPrice());
        existing.setParkingType(parking.getParkingType());

        existing.getFloors().clear();
        if(parking.getFloors()!=null){
            for(Floor f:parking.getFloors()){
                f.setParking(existing);
                existing.getFloors().add(f);
            }
        }

        NormalSlot slot=parking.getNormalSlot();
        if(slot!=null){
            slot.setParking(existing);
            existing.setNormalSlot(slot);
        }

        return parkingRepository.save(existing);
    }

    public void deleteParking(Long id){
        parkingRepository.deleteById(id);
    }

    public List<Parking> getAllParkings(){
        return parkingRepository.findAll();
    }

    public Parking getParkingById(Long id){
        return parkingRepository.findById(id).orElseThrow();
    }

    public Long totalSlots(){
        return parkingRepository.getTotalSlots();
    }

    public Long availableSlots(){
        return parkingRepository.getAvailableSlots();
    }

    public Long bookedSlots(){
        return parkingRepository.getBookedSlots();
    }
}