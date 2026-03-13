package com.infosys.ParkEasy.service.imp;

import com.infosys.ParkEasy.dto.Reponse.*;
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
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminServiceImp implements AdminService {

    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final ParkingRepository parkingRepository;
    private final ParkingSpotRepository parkingSpotRepository;
    private final PaymentOrderRepository paymentOrderRepository;
    private final ModelMapper modelMapper;

    //Dashboard cards
    @Override
    public DashboardStatsResponseDto getDashboardStats(){
        List<Object[]> result=bookingRepository.getDashboardStats();
        Object[] stats=result.get(0);
        return new DashboardStatsResponseDto(
                ((Number)stats[0]).longValue(),
                ((Number)stats[1]).longValue(),
                ((Number)stats[2]).longValue(),
                ((Number)stats[3]).longValue(),
                ((Number)stats[4]).longValue(),
                ((Number)stats[5]).longValue(),
                ((Number)stats[6]).doubleValue()
        );
    }

    //Register parking
    @Override
    @Transactional
    public Parking registerParking(ParkingRequestDto dto){

        Parking parking=new Parking();
        parking.setParkingName(dto.getParkingName());
        parking.setAddress(dto.getAddress());
        parking.setCity(dto.getCity());
        parking.setPhone(dto.getPhone());
        parking.setPinCode(dto.getPinCode());
        parking.setPrice(dto.getPrice());
        parking.setOpenTime(dto.getOpenTime());
        parking.setCloseTime(dto.getCloseTime());
        parking.setEvEnabled(dto.getEvEnabled());
        parking.setEvPrice(dto.getEvPrice());
        parking.setParkingType(dto.getParkingType());

        Parking savedParking=parkingRepository.save(parking);
        List<ParkingSpot> spots=new ArrayList<>();

        if(dto.getNormalSlots()!=null){
            String prefix=dto.getNormalSlots().getPrefix();
            Integer totalSlots=dto.getNormalSlots().getTotalSlots();
            Integer evSlots=dto.getNormalSlots().getEvStations();

            if(totalSlots==null||totalSlots<=0){
                throw new RuntimeException("Total slots required");
            }

            if(evSlots==null)evSlots=0;

            for(int i=1;i<=totalSlots;i++){
                ParkingSpot spot=new ParkingSpot();
                spot.setStatus(SpotStatus.AVAILABLE);
                spot.setParking(parking);

                if(i<=evSlots){
                    spot.setSlotType(SlotType.EV);
                    spot.setSpotNumber(prefix+i+"-EV");
                }else{
                    spot.setSlotType(SlotType.NORMAL);
                    spot.setSpotNumber(prefix+i);
                }

                spots.add(spot);
            }
        }

        if(dto.getFloors()!=null&&!dto.getFloors().isEmpty()){
            for(FloorRequestDto floor:dto.getFloors()){

                String prefix=floor.getPrefix();
                Integer totalSlots=floor.getTotalSlots();
                Integer evSlots=floor.getEvStations();

                if(totalSlots==null||totalSlots<=0){
                    throw new RuntimeException("Total slots required");
                }

                if(evSlots==null)evSlots=0;

                for(int i=1;i<=totalSlots;i++){
                    ParkingSpot spot=new ParkingSpot();
                    spot.setStatus(SpotStatus.AVAILABLE);
                    spot.setParking(parking);
                    spot.setFloorName(floor.getFloorName());

                    if(i<=evSlots){
                        spot.setSlotType(SlotType.EV);
                        spot.setSpotNumber(prefix+i+"-EV");
                    }else{
                        spot.setSlotType(SlotType.NORMAL);
                        spot.setSpotNumber(prefix+i);
                    }

                    spots.add(spot);
                }
            }
        }

        parkingSpotRepository.saveAll(spots);
        return savedParking;
    }

    //Update parking
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

        return parkingRepository.save(existing);
    }

    @Override
    public void deleteParking(Long id){
        parkingRepository.deleteById(id);
    }

    @Override
    public List<ParkingsResponseDto> getAllParkings(){
        return parkingRepository.getRealtimeParkingStatus();
    }

    @Override
    public Parking getParkingById(Long id){
        return parkingRepository.findById(id).orElseThrow();
    }

    //User profile
    @Override
    public UserProfileResponseDto getUserDetails(String customId){
        User user=userRepository.findByCustomId(customId)
                .orElseThrow(()->new UsernameNotFoundException("User Not Exist"));
        return modelMapper.map(user,UserProfileResponseDto.class);
    }

    //Admin user list
    @Override
    public ResponseEntity<List<AdminUserResponseDto>> getAllUserDetails(){

        List<User> users=userRepository.findAll();

        List<AdminUserResponseDto> response=users.stream().map(user->{

            AdminUserResponseDto dto=new AdminUserResponseDto();
            dto.setCustomId(user.getCustomId());
            dto.setName(user.getName());
            dto.setPhone(user.getPhone());
            dto.setUserStatusType(user.getStatusType());

            List<VehicleResponseDto> vehicles=user.getVehicles().stream().map(v->{
                VehicleResponseDto vr=new VehicleResponseDto();
                vr.setVehicleNumber(v.getVehicleNumber());
                vr.setVehicleType(v.getVehicleType());
                vr.setBrand(v.getBrand());
                vr.setModel(v.getModel());
                vr.setColor(v.getColor());
                return vr;
            }).toList();

            dto.setVehicleDetails(vehicles);

            List<AddressResponseDto> addresses=user.getAddresses().stream().map(a->{
                AddressResponseDto ar=new AddressResponseDto();
                ar.setAddressLine1(a.getAddressLine1());
                ar.setAddressLine2(a.getAddressLine2());
                ar.setCity(a.getCity());
                ar.setState(a.getState());
                ar.setCountry(a.getCountry());
                ar.setPinCode(a.getPinCode());
                ar.setAddressType(a.getAddressType());
                return ar;
            }).toList();

            dto.setAddress(addresses);

            return dto;

        }).toList();

        return ResponseEntity.ok(response);
    }

    //Dashboard charts
    @Override
    public DashboardResponse dashboard(){

        List<Object[]> bookingData = bookingRepository.monthlyBookings();

        List<Integer> bookings = new ArrayList<>();
        List<String> months = new ArrayList<>();

        for(Object[] row : bookingData){
            months.add(String.valueOf(row[0]));
            bookings.add(((Number)row[1]).intValue());
        }


        List<Object[]> hourlyData = bookingRepository.hourlyBookings();

        List<Integer> hourlyBookings = new ArrayList<>();
        List<String> hours = new ArrayList<>();

        for(Object[] row : hourlyData){

            int hour = ((Number)row[0]).intValue();

            String label;
            if(hour == 0) label = "12AM";
            else if(hour < 12) label = hour + "AM";
            else if(hour == 12) label = "12PM";
            else label = (hour - 12) + "PM";

            hours.add(label);
            hourlyBookings.add(((Number)row[1]).intValue());
        }


        List<Object[]> parkingData = bookingRepository.parkingBookings();

        List<Integer> locationBookings = new ArrayList<>();
        List<String> locations = new ArrayList<>();

        for(Object[] row : parkingData){
            locations.add("Parking " + row[0]);
            locationBookings.add(((Number)row[1]).intValue());
        }


        List<Object[]> revenueData = paymentOrderRepository.monthlyRevenue();

        List<Double> revenue = new ArrayList<>();

        for(Object[] row : revenueData){
            revenue.add(((Number)row[1]).doubleValue());
        }


        long dailyUsers = userRepository.countByCreatedAtAfter(LocalDateTime.now().minusDays(1));
        long weeklyUsers = userRepository.countByCreatedAtAfter(LocalDateTime.now().minusWeeks(1));
        long monthlyUsers = userRepository.countByCreatedAtAfter(LocalDateTime.now().minusMonths(1));


        return DashboardResponse.builder()
                .months(months)
                .monthlyBookings(bookings)
                .hours(hours)
                .hourlyBookings(hourlyBookings)
                .locations(locations)
                .locationBookings(locationBookings)
                .monthlyRevenue(revenue)
                .dailyUsers((int)dailyUsers)
                .weeklyUsers((int)weeklyUsers)
                .monthlyUsers((int)monthlyUsers)
                .build();
    }
}