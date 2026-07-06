package com.sarthak.agenticai.service.impl;

import com.razorpay.RazorpayClient;
import com.sarthak.agenticai.dto.PaymentRequestDto;
import com.sarthak.agenticai.dto.PaymentResponseDto;
import com.sarthak.agenticai.entity.*;
import com.sarthak.agenticai.exception.ResourceNotFoundException;
import com.sarthak.agenticai.repository.OrderRepository;
import com.sarthak.agenticai.repository.PaymentRepository;
import com.sarthak.agenticai.repository.UserRepository;
import com.sarthak.agenticai.service.PaymentService;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import com.razorpay.Utils;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final RazorpayClient razorpayClient;
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public PaymentServiceImpl(
            RazorpayClient razorpayClient,
            PaymentRepository paymentRepository,
            OrderRepository orderRepository,
            UserRepository userRepository) {

        this.razorpayClient = razorpayClient;
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }
    @Override
    public PaymentResponseDto createPaymentOrder(
            String email,
            PaymentRequestDto request) {

        // Find User
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        // Find Order
        Order order = orderRepository
                .findByIdAndUser(request.getOrderId(), user)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Order not found"));

        // Check Order Owner

        // Check Existing Payment
        if (paymentRepository.findByOrder(order).isPresent()) {
            throw new RuntimeException("Payment already created");
        }

        // Razorpay Request
        JSONObject options = new JSONObject();

        options.put("amount",
                (int) (order.getTotalAmount() * 100));

        options.put("currency", "INR");

        options.put("receipt",
                "order_" + order.getId());

        // Create Razorpay Order
        com.razorpay.Order razorpayOrder;

        try {
            razorpayOrder = razorpayClient.orders.create(options);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create Razorpay order", e);
        }

        // Save Payment
        Payment payment = new Payment();

        payment.setOrder(order);

        payment.setRazorpayOrderId(
                razorpayOrder.get("id").toString());

        payment.setAmount(
                order.getTotalAmount());

        payment.setStatus(
                PaymentStatus.PENDING);

        Payment savedPayment =
                paymentRepository.save(payment);

        return new PaymentResponseDto(

                savedPayment.getId(),

                savedPayment.getRazorpayOrderId(),

                savedPayment.getAmount(),

                savedPayment.getStatus().name()

        );
    }
    @Override
    public PaymentResponseDto verifyPayment(
            String razorpayOrderId,
            String razorpayPaymentId,
            String razorpaySignature) {

        Payment payment = paymentRepository
                .findByRazorpayOrderId(razorpayOrderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Payment not found"));
        

        payment.setRazorpayPaymentId(razorpayPaymentId);
        payment.setRazorpaySignature(razorpaySignature);
        payment.setStatus(PaymentStatus.SUCCESS);
        payment.setPaymentDate(java.time.LocalDateTime.now());

        paymentRepository.save(payment);

        Order order = payment.getOrder();

        order.setStatus(OrderStatus.CONFIRMED);

        orderRepository.save(order);

        return new PaymentResponseDto(

                payment.getId(),

                payment.getRazorpayOrderId(),

                payment.getAmount(),

                payment.getStatus().name()

        );
    }
}