package com.xalts.bookingservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentDTO {

    private Long bookingId;
    private Double amount;
    private String transactionId;  // Unique transaction ID
    private String status;         // Payment status (SUCCESS, FAILED, PENDING)



}
