package com.xalts.paymentservice.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {
    private Long bookingId;
    private Double amount;
    private String status;
    private String transactionId;
    private LocalDateTime paymentDate;
}
