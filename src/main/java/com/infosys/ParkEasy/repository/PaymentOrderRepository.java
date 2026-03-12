package com.infosys.ParkEasy.repository;

import com.infosys.ParkEasy.entity.PaymentOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface PaymentOrderRepository extends JpaRepository<PaymentOrder,Long> {
     Optional<PaymentOrder> findByOrderId(String orderId);

    List<PaymentOrder> findByUserId(Long id);
}
