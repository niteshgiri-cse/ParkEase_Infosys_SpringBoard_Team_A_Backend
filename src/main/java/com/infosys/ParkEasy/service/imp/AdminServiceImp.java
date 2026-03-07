package com.infosys.ParkEasy.service.imp;

import com.infosys.ParkEasy.dto.Reponse.ChartResponseDto;
import com.infosys.ParkEasy.dto.Reponse.DashboardStatsResponseDto;
import com.infosys.ParkEasy.dto.Reponse.UserProfileResponseDto;
import com.infosys.ParkEasy.dto.Request.FloorRequestDto;
import com.infosys.ParkEasy.dto.Request.ParkingRequestDto;
import com.infosys.ParkEasy.entity.*;
import com.infosys.ParkEasy.entity.type.SlotType;
import com.infosys.ParkEasy.entity.type.SpotStatus;
import com.infosys.ParkEasy.repository.*;
import com.infosys.ParkEasy.service.Interface.AdminService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminServiceImp implements AdminService {

    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final ParkingRepository parkingRepository;
    private final SlotRepository slotRepository;
    private final ModelMapper modelMapper;
    private final ParkingSpotRepository parkingSpotRepository;


    @Override
    public DashboardStatsResponseDto getDashboardStats() {

        long totalUsers = userRepository.count();
        long totalBookings = bookingRepository.getTotalConfirmedBookings();
        long totalLocations = parkingRepository.count();
        long totalSlots = parkingRepository.getTotalSlots();
        long bookedSlots = slotRepository.countByBookedTrue();
        long availableSlots = slotRepository.countByBookedFalse();
        double revenue = bookingRepository.getTotalRevenue();
        return new DashboardStatsResponseDto(
                totalLocations,
                totalSlots,
                availableSlots,
                bookedSlots,
                totalUsers,
                totalBookings,
                revenue
        );
    }
    @Override
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

    @Override
    @Transactional
    public Parking registerParking(ParkingRequestDto parkingRequestDto) {

        Parking parking = getParking(parkingRequestDto);
        Parking saveParking = parkingRepository.save(parking);

        List<ParkingSpot> spots = new ArrayList<>();

        // -------- NORMAL PARKING (NO FLOOR) --------

        if (parkingRequestDto.getNormalSlots() != null) {

            String prefix = parkingRequestDto.getNormalSlots().getPrefix();
            Integer totalSlots = parkingRequestDto.getNormalSlots().getTotalSlots();
            Integer evSlots = parkingRequestDto.getNormalSlots().getEvStations();

            if (evSlots == null) {
                evSlots = 0;
            }

            for (int i = 1; i <= totalSlots; i++) {

                ParkingSpot spot = new ParkingSpot();
                spot.setStatus(SpotStatus.AVAILABLE);
                spot.setParking(parking);

                // EV SLOT
                if (i <= evSlots) {
                    spot.setSlotType(SlotType.EV);
                    spot.setSpotNumber(prefix + i + "-EV");
                }
                // NORMAL SLOT
                else {
                    spot.setSlotType(SlotType.NORMAL);
                    spot.setSpotNumber(prefix + i);
                }

                spots.add(spot);
            }
        }

        // -------- FLOOR WISE PARKING --------

        if (parkingRequestDto.getFloors() != null && !parkingRequestDto.getFloors().isEmpty()) {

            for (FloorRequestDto floor : parkingRequestDto.getFloors()) {

                String prefix = floor.getPrefix();
                Integer totalSlots = floor.getTotalSlots();
                Integer evSlots = floor.getEvStations();

                if (evSlots == null) {
                    evSlots = 0;
                }

                for (int i = 1; i <= totalSlots; i++) {

                    ParkingSpot spot = new ParkingSpot();
                    spot.setStatus(SpotStatus.AVAILABLE);
                    spot.setParking(parking);
                    spot.setFloorName(floor.getFloorName());

                    // EV SLOT
                    if (i <= evSlots) {
                        spot.setSlotType(SlotType.EV);
                        spot.setSpotNumber(prefix + i + "-EV");
                    }
                    // NORMAL SLOT
                    else {
                        spot.setSlotType(SlotType.NORMAL);
                        spot.setSpotNumber(prefix + i);
                    }

                    spots.add(spot);
                }
            }
        }

        parkingSpotRepository.saveAll(spots);

        return saveParking;
    }



    private static Parking getParking(ParkingRequestDto requestDto) {
        Parking parking = new Parking();

        parking.setParkingName(requestDto.getParkingName());
        parking.setAddress(requestDto.getAddress());
        parking.setCity(requestDto.getCity());
        parking.setPhone(requestDto.getPhone());
        parking.setPinCode(requestDto.getPinCode());
        parking.setPrice(requestDto.getPrice());
        parking.setOpenTime(requestDto.getOpenTime());
        parking.setCloseTime(requestDto.getCloseTime());
        parking.setEvEnabled(requestDto.getEvEnabled());
        parking.setEvPrice(requestDto.getEvPrice());
        parking.setParkingType(requestDto.getParkingType());
        return parking;
    }

    @Override
    public Parking updateParking(Long id,Parking parking){
        Parking existing=parkingRepository.findById(id).orElseThrow();
        existing.setParkingName(parking.getParkingName());
        existing.setAddress(parking.getAddress());
        existing.setCity(parking.getCity());
        existing.setPhone(parking.getPhone());
        existing.setPinCode(parking.getPinCode());
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
    @Override
    public void deleteParking(Long id){
        parkingRepository.deleteById(id);
    }
    @Override
    public List<Parking> getAllParkings(){
        return parkingRepository.findAll();
    }
    @Override
    public Parking getParkingById(Long id){
        return parkingRepository.findById(id).orElseThrow();
    }
    @Override
    public Long totalSlots(){
        return parkingRepository.getTotalSlots();
    }
    @Override
    public Long availableSlots(){
        return parkingRepository.getAvailableSlots();
    }
    @Override
    public Long bookedSlots(){
        return parkingRepository.getBookedSlots();
    }
    @Override
    public UserProfileResponseDto getUserDetails(String customId) {
       User user=userRepository.findByCustomId(customId).orElseThrow(()->new UsernameNotFoundException("User Not " +
               "Exist"));
        return modelMapper.map(user,UserProfileResponseDto.class);
    }
}