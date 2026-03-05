package com.infosys.ParkEasy.service.imp;

import com.infosys.ParkEasy.dto.Reponse.UserProfileResponseDto;
import com.infosys.ParkEasy.entity.User;
import com.infosys.ParkEasy.repository.UserRepository;
import com.infosys.ParkEasy.service.Interface.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    @Override
    @Transactional(readOnly = true)
    public UserProfileResponseDto getUserProfile() {
        String email=SecurityContextHolder.getContext().getAuthentication().getName();
        User user=userRepository.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("User not Found"));
        UserProfileResponseDto response = modelMapper.map(user, UserProfileResponseDto.class);
        response.setRoles(user.getRoleTypes().stream().toList());
        return response;
    }
}
