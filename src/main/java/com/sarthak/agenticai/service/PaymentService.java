package com.sarthak.agenticai.service;

import com.sarthak.agenticai.dto.PaymentRequestDto;
import com.sarthak.agenticai.dto.PaymentResponseDto;

import java.util.List;

public interface PaymentService {

    PaymentResponseDto createPaymentOrder(
            String email,
            PaymentRequestDto request);

    PaymentResponseDto verifyPayment(
            String razorpayOrderId,
            String razorpayPaymentId,
            String razorpaySignature);
    List<PaymentResponseDto> getMyPayments(String email);

    PaymentResponseDto getPaymentByOrderId(
            String email,
            Long orderId);
}