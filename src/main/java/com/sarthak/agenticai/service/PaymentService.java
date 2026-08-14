package com.sarthak.agenticai.service;

import com.sarthak.agenticai.dto.PaymentRequestDto;
import com.sarthak.agenticai.dto.PaymentResponseDto;

import java.util.List;

public interface PaymentService {

    PaymentResponseDto createPaymentOrder(
            String email,
            PaymentRequestDto request);
    List<PaymentResponseDto> getMyPayments(String email);

    PaymentResponseDto getPaymentByOrderId(
            String email,
            Long orderId);
    PaymentResponseDto verifyPayment(
            String email,
            String razorpayOrderId,
            String razorpayPaymentId,
            String razorpaySignature
    );
}