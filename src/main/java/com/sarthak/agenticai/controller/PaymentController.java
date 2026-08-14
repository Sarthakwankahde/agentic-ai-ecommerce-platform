package com.sarthak.agenticai.controller;

import com.sarthak.agenticai.dto.PaymentRequestDto;
import com.sarthak.agenticai.dto.PaymentResponseDto;
import com.sarthak.agenticai.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }
    
    @PostMapping("/create-order")
    public PaymentResponseDto createPaymentOrder(
            Authentication authentication,
            @Valid @RequestBody PaymentRequestDto request) {

        String email = authentication.getName();

        return paymentService.createPaymentOrder(
                email,
                request
        );
    }
    @PostMapping("/verify")
    public PaymentResponseDto verifyPayment(
            Authentication authentication,
            @RequestParam String razorpayOrderId,
            @RequestParam String razorpayPaymentId,
            @RequestParam String razorpaySignature) {

        String email = authentication.getName();

        return paymentService.verifyPayment(
                email,
                razorpayOrderId,
                razorpayPaymentId,
                razorpaySignature
        );
    }
}