package com.sarthak.agenticai.ai.tool;

import com.sarthak.agenticai.dto.PaymentRequestDto;
import com.sarthak.agenticai.dto.PaymentResponseDto;
import com.sarthak.agenticai.service.PaymentService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PaymentTool {

    private final PaymentService paymentService;

    public PaymentTool(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Tool(description = "Creates a payment order for an existing order")
    public String createPaymentOrder(
            String email,
            Long orderId) {

        PaymentRequestDto request = new PaymentRequestDto();
        request.setOrderId(orderId);

        PaymentResponseDto payment =
                paymentService.createPaymentOrder(email, request);

        return buildPaymentResponse(List.of(payment));
    }

    @Tool(description = "Verify a Razorpay payment after successful payment")
    public String verifyPayment(
            String razorpayOrderId,
            String razorpayPaymentId,
            String razorpaySignature) {

        PaymentResponseDto payment =
                paymentService.verifyPayment(
                        razorpayOrderId,
                        razorpayPaymentId,
                        razorpaySignature
                );

        return buildPaymentResponse(List.of(payment));
    }

    @Tool(description = "Returns all payments made by the logged-in user")
    public String getMyPayments(String email) {

        return buildPaymentResponse(
                paymentService.getMyPayments(email)
        );
    }

    @Tool(description = "Returns payment details for a specific order")
    public String getPaymentByOrder(
            String email,
            Long orderId) {

        PaymentResponseDto payment =
                paymentService.getPaymentByOrderId(email, orderId);

        return buildPaymentResponse(List.of(payment));
    }

    private String buildPaymentResponse(
            List<PaymentResponseDto> payments) {

        if (payments.isEmpty()) {
            return "No payment records found.";
        }

        StringBuilder response = new StringBuilder();

        int index = 1;

        for (PaymentResponseDto payment : payments) {

            response.append("""
                    
                    Payment %d
                    
                    Payment ID : %d
                    Razorpay Order : %s
                    Amount : ₹%s
                    Status : %s
                    
                    """.formatted(
                    index++,
                    payment.getPaymentId(),
                    payment.getRazorpayOrderId(),
                    payment.getAmount(),
                    payment.getStatus()
            ));
        }

        return response.toString();
    }
}