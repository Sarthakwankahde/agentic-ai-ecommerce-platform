package com.sarthak.agenticai.service;

import com.sarthak.agenticai.dto.PaymentRequestDto;
import com.sarthak.agenticai.dto.PaymentResponseDto;

public interface PaymentService {

    PaymentResponseDto createPaymentOrder(
            String email,
            PaymentRequestDto request);

    PaymentResponseDto verifyPayment(
            String razorpayOrderId,
            String razorpayPaymentId,
            String razorpaySignature);
}