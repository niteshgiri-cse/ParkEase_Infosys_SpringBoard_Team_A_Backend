package com.infosys.ParkEasy.dto.Reponse;

import com.infosys.ParkEasy.entity.type.PaymentStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Data
public class PaymentHistory {
    private Long id;

    private String paymentId;

    private Double amount;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
}
