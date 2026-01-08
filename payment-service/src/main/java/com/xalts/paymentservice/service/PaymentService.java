package com.xalts.paymentservice.service;

import com.xalts.paymentservice.dto.PaymentDTO;

public interface PaymentService {
    PaymentDTO processPayment(PaymentDTO paymentDTO);
    PaymentDTO getPaymentByTransactionId(String transactionId);

}
