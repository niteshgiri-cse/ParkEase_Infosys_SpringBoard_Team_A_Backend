package com.infosys.ParkEasy.service.imp;

import com.infosys.ParkEasy.dto.Reponse.AddressResponseDto;
import com.infosys.ParkEasy.dto.Request.AddressRequestDto;
import com.infosys.ParkEasy.entity.Address;
import com.infosys.ParkEasy.entity.User;
import com.infosys.ParkEasy.repository.AddressRepository;
import com.infosys.ParkEasy.repository.UserRepository;
import com.infosys.ParkEasy.service.Interface.AddressService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressServiceImp implements AddressService {
    private final ModelMapper modelMapper;
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    @Override
    public AddressResponseDto createAddress(AddressRequestDto addressRequestDto) {
    String email= SecurityContextHolder.getContext().getAuthentication().getName();
    User user=userRepository.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("User not found"));
        long addressCount = addressRepository.countByUserId(user.getId());

        if(addressCount >= 2){
            throw new RuntimeException("User can store maximum 2 addresses");
        }
        Address address=Address.builder()
                .addressLine1(addressRequestDto.getAddressLine1())
                .addressLine2(addressRequestDto.getAddressLine2())
                .city(addressRequestDto.getCity())
                .country(addressRequestDto.getCountry())
                .state(addressRequestDto.getState())
                .pinCode(addressRequestDto.getPinCode())
                .addressType(addressRequestDto.getAddressType())
                .user(user)
                .build();
        Address save=addressRepository.save(address);
        return modelMapper.map(save, AddressResponseDto.class);
    }

    @Override
    public List<AddressResponseDto> getUserAddresses() {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        List<Address> addresses = addressRepository.findAllByUserId(user.getId());

        return addresses.stream()
                .map(address -> modelMapper.map(address, AddressResponseDto.class))
                .toList();
    }

    @Override
    public AddressResponseDto updateAddress(Long addressId, AddressRequestDto addressRequestDto) {
        String email=SecurityContextHolder.getContext().getAuthentication().getName();
        User user=userRepository.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("User not found"));
        Address existAddress=addressRepository.findById(addressId).orElseThrow(()->new RuntimeException("Address " +
                "not " +
                "found"));
        if(!existAddress.getUser().getId().equals(user.getId()))  throw new RuntimeException("Unauthorized access");
        modelMapper.map(addressRequestDto,existAddress);
        Address save=addressRepository.save(existAddress);
        return modelMapper.map(save, AddressResponseDto.class);
    }

    @Override
    public void deleteAddress(Long addressId) {
        String email=SecurityContextHolder.getContext().getAuthentication().getName();
        User user=userRepository.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("User not found"));
        Address existAddress=addressRepository.findById(addressId).orElseThrow(()->new RuntimeException("Address " +
                "not " +
                "found"));
        if(!existAddress.getUser().getId().equals(user.getId()))  throw new RuntimeException("Unauthorized access");
        addressRepository.deleteById(addressId);
    }
}
