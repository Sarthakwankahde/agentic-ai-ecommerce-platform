package com.sarthak.agenticai.repository;

import com.sarthak.agenticai.entity.Order;
import com.sarthak.agenticai.entity.Payment;
import com.sarthak.agenticai.entity.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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
    @Query("""
SELECT COALESCE(SUM(p.amount),0)
FROM Payment p
WHERE p.status = com.sarthak.agenticai.entity.PaymentStatus.SUCCESS
""")
    Double getTotalSuccessfulPaymentAmount();

}