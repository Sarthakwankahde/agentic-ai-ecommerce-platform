package com.sarthak.agenticai.controller;

import com.sarthak.agenticai.dto.PaymentRequestDto;
import com.sarthak.agenticai.dto.PaymentResponseDto;
import com.sarthak.agenticai.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/create-order")
    public PaymentResponseDto createPaymentOrder(

            @RequestParam String email,

            @Valid
            @RequestBody PaymentRequestDto request) {

        return paymentService.createPaymentOrder(
                email,
                request);
    }

    @PostMapping("/verify")
    public PaymentResponseDto verifyPayment(

            @RequestParam String razorpayOrderId,

            @RequestParam String razorpayPaymentId,

            @RequestParam String razorpaySignature) {

        return paymentService.verifyPayment(

                razorpayOrderId,

                razorpayPaymentId,

                razorpaySignature

        );
    }
}