package com.infosys.ParkEasy.service.imp;

import com.infosys.ParkEasy.dto.Reponse.VehicleResponseDto;
import com.infosys.ParkEasy.dto.Request.VehicleRequestDto;
import com.infosys.ParkEasy.entity.User;
import com.infosys.ParkEasy.entity.Vehicle;
import com.infosys.ParkEasy.repository.UserRepository;
import com.infosys.ParkEasy.repository.VehicleRepository;
import com.infosys.ParkEasy.service.Interface.VehicleService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VehicleServiceImp implements VehicleService {
    private final VehicleRepository vehicleRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public VehicleResponseDto registerVehicle(VehicleRequestDto vehicleRequestDto) {
       String email=SecurityContextHolder.getContext().getAuthentication().getName();
       User user=userRepository.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("User Not found"));
        Vehicle vehicle=Vehicle.builder()
                .vehicleNumber(vehicleRequestDto.getVehicleNumber())
                .vehicleType(vehicleRequestDto.getVehicleType())
                .brand(vehicleRequestDto.getBrand())
                .model(vehicleRequestDto.getModel())
                .user(user)
                .color(vehicleRequestDto.getColor())
                .build();
        Vehicle saveVehicle=vehicleRepository.save(vehicle);
        return modelMapper.map(saveVehicle, VehicleResponseDto.class);
    }

    @Override
    public void deleteVehicle(Long vehicleId) {
        String email=SecurityContextHolder.getContext().getAuthentication().getName();
        User user=userRepository.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("User Not found"));
        Vehicle existVehicle=vehicleRepository.findById(vehicleId).orElseThrow(()-> new RuntimeException("Vehicle not" +
                " " +
                "found"));
        if(!user.getId().equals(existVehicle.getUser().getId())) throw new RuntimeException("Unauthorized access");
        vehicleRepository.delete(existVehicle);

    }
}
