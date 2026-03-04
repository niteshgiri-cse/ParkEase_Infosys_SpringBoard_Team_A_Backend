package com.infosys.ParkEasy.repository;

import com.infosys.ParkEasy.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface AddressRepository extends JpaRepository<Address,Long> {

    Optional<Address> findByUserId(Long id);

    List<Address> findAllByUserId(Long id);

    long countByUserId(Long id);
}
