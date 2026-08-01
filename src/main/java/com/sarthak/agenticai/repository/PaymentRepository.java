package com.sarthak.agenticai.repository;

import com.sarthak.agenticai.entity.Order;
import com.sarthak.agenticai.entity.Payment;
import com.sarthak.agenticai.entity.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    // Find payment by Order
    Optional<Payment> findByOrder(Order order);

    // Find payment using Razorpay Order Id
    Optional<Payment> findByRazorpayOrderId(
            String razorpayOrderId
    );
    Optional<Payment> findByRazorpayPaymentId(
            String razorpayPaymentId
    );
    long countByStatus(PaymentStatus status);

}