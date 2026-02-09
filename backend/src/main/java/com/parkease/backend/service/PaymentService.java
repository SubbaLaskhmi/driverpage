package com.parkease.backend.service;

import com.parkease.backend.entity.Booking;
import com.parkease.backend.entity.Payment;
import com.parkease.backend.enumtype.PaymentStatus;
import com.parkease.backend.repository.PaymentRepository;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public Payment createPayment(
            Booking booking,
            double totalAmount,
            double platformFee,
            String method
    ) {

        Payment payment = new Payment();
        payment.setBooking(booking);
        payment.setTotalAmount(totalAmount);
        payment.setPlatformFee(platformFee);
        payment.setProviderEarning(totalAmount - platformFee);
        payment.setPaymentMethod(method);
        payment.setStatus(PaymentStatus.PAID);

        return paymentRepository.save(payment);
    }
}
