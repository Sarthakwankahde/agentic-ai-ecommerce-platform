package com.sarthak.agenticai.repository;

import com.sarthak.agenticai.entity.Order;
import com.sarthak.agenticai.entity.Payment;
import com.sarthak.agenticai.entity.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.sarthak.agenticai.entity.User;

import java.util.List;
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
    List<Payment> findByOrder_User(User user);

    Optional<Payment> findByOrder_IdAndOrder_User(
            Long orderId,
            User user
    );
    @Query("""
SELECT COALESCE(SUM(p.amount),0)
FROM Payment p
WHERE p.status = com.sarthak.agenticai.entity.PaymentStatus.SUCCESS
""")
    Double getTotalSuccessfulPaymentAmount();

}