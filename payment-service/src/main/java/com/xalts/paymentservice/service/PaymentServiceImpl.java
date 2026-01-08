package com.xalts.paymentservice.service;

import com.xalts.paymentservice.dto.PaymentDTO;
import com.xalts.paymentservice.exception.EntityNotFoundException;
import com.xalts.paymentservice.model.Payment;
import com.xalts.paymentservice.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;


    @Override
    public PaymentDTO processPayment(PaymentDTO paymentDTO) {
        Payment payment = new Payment();
        payment.setBookingId(paymentDTO.getBookingId());
        payment.setAmount(paymentDTO.getAmount()); // We can implement different 3rd party payment vendors
        payment.setStatus("SUCCESS");
        payment.setTransactionId(UUID.randomUUID().toString());
        payment.setPaymentDate(LocalDateTime.now());

        paymentRepository.save(payment);

        return new PaymentDTO(payment.getBookingId(), payment.getAmount(), payment.getStatus(),
                payment.getTransactionId(), payment.getPaymentDate());
    }

    @Override
    public PaymentDTO getPaymentByTransactionId(String transactionId) {
        Payment payment = paymentRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new EntityNotFoundException("Payment not found for Transaction ID: " + transactionId));

        return new PaymentDTO(payment.getBookingId(), payment.getAmount(), payment.getStatus(),
                payment.getTransactionId(), payment.getPaymentDate());
    }


}
