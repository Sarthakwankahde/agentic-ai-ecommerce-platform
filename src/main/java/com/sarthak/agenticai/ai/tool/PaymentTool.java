package com.sarthak.agenticai.ai.tool;

import com.sarthak.agenticai.entity.PaymentStatus;
import com.sarthak.agenticai.repository.PaymentRepository;
import org.springframework.stereotype.Component;

@Component
public class PaymentTool {

    private final PaymentRepository paymentRepository;

    public PaymentTool(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public String getPaymentSummary() {

        long successPayments =
                paymentRepository.countByStatus(PaymentStatus.SUCCESS);

        long failedPayments =
                paymentRepository.countByStatus(PaymentStatus.FAILED);

        long pendingPayments =
                paymentRepository.countByStatus(PaymentStatus.PENDING);

        Double totalAmount =
                paymentRepository.getTotalSuccessfulPaymentAmount();

        return """
                Payment Summary

                Successful Payments : %d
                Failed Payments     : %d
                Pending Payments    : %d

                Total Revenue from Successful Payments : ₹%.2f
                """
                .formatted(
                        successPayments,
                        failedPayments,
                        pendingPayments,
                        totalAmount
                );
    }
}