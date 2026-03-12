package com.infosys.ParkEasy.service.imp;

import com.infosys.ParkEasy.dto.Reponse.*;
import com.infosys.ParkEasy.entity.Booking;
import com.infosys.ParkEasy.entity.PaymentOrder;
import com.infosys.ParkEasy.entity.User;
import com.infosys.ParkEasy.repository.BookingRepository;
import com.infosys.ParkEasy.repository.PaymentOrderRepository;
import com.infosys.ParkEasy.repository.UserRepository;
import com.infosys.ParkEasy.service.Interface.UserService;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final PaymentOrderRepository paymentOrderRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional(readOnly = true)
    public UserProfileResponseDto getUserProfile() {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not Found"));

        UserProfileResponseDto response = modelMapper.map(user, UserProfileResponseDto.class);

        // roles
        response.setRoles(user.getRoleTypes().stream().toList());

        // addresses
        response.setAddresses(
                user.getAddresses()
                        .stream()
                        .map(address -> modelMapper.map(address, AddressResponseDto.class))
                        .toList()
        );

        // vehicles
        response.setVehicles(
                user.getVehicles()
                        .stream()
                        .map(vehicle -> modelMapper.map(vehicle, VehicleResponseDto.class))
                        .toList()
        );

        return response;
    }

    @Override
    public List<BookingResponseDto> getAllBooking() {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not Found"));
        List<Booking> bookings=bookingRepository.findByUserId(user.getId());
        return bookings.stream().map(booking -> modelMapper.map(booking, BookingResponseDto.class)).toList();
    }

    @Override
    public List<PaymentHistory> getAllPaymentHistory() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not Found"));

        List<PaymentOrder> paymentOrders = paymentOrderRepository.findByUserId(user.getId());

        return paymentOrders.stream()
                .map(order -> modelMapper.map(order, PaymentHistory.class))
                .toList();
    }
}