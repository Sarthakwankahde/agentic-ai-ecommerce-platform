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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.razorpay.Utils;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final RazorpayClient razorpayClient;
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    @Value("${razorpay.key.secret}")
    private String razorpayKeySecret;

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
    @Transactional
    public PaymentResponseDto verifyPayment(
            String email,
            String razorpayOrderId,
            String razorpayPaymentId,
            String razorpaySignature) {

        // 1. Find logged-in user
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        // 2. Find payment using Razorpay Order ID
        Payment payment = paymentRepository
                .findByRazorpayOrderId(razorpayOrderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Payment not found"));

        // 3. Verify that payment belongs to logged-in user
        if (!payment.getOrder()
                .getUser()
                .getId()
                .equals(user.getId())) {

            throw new RuntimeException(
                    "You are not authorized to verify this payment"
            );
        }

        // 4. Prevent duplicate verification
        if (payment.getStatus() == PaymentStatus.SUCCESS) {

            throw new RuntimeException(
                    "Payment is already verified"
            );
        }

        try {

            // 5. Prepare Razorpay signature verification data
            JSONObject options = new JSONObject();

            options.put(
                    "razorpay_order_id",
                    razorpayOrderId
            );

            options.put(
                    "razorpay_payment_id",
                    razorpayPaymentId
            );

            options.put(
                    "razorpay_signature",
                    razorpaySignature
            );

            // 6. Verify Razorpay signature
            boolean isValid =
                    Utils.verifyPaymentSignature(
                            options,
                            razorpayKeySecret
                    );

            // 7. If signature is invalid
            if (!isValid) {

                payment.setStatus(
                        PaymentStatus.FAILED
                );

                paymentRepository.save(payment);

                throw new RuntimeException(
                        "Payment signature verification failed"
                );
            }

            // 8. Store Razorpay payment details
            payment.setRazorpayPaymentId(
                    razorpayPaymentId
            );

            payment.setRazorpaySignature(
                    razorpaySignature
            );

            payment.setStatus(
                    PaymentStatus.SUCCESS
            );

            payment.setPaymentDate(
                    java.time.LocalDateTime.now()
            );

            paymentRepository.save(payment);

            // 9. Update order status
            Order order = payment.getOrder();

            order.setStatus(
                    OrderStatus.CONFIRMED
            );

            orderRepository.save(order);

            // 10. Return payment response
            return new PaymentResponseDto(
                    payment.getId(),
                    payment.getRazorpayOrderId(),
                    payment.getAmount(),
                    payment.getStatus().name()
            );

        } catch (com.razorpay.RazorpayException e) {

            throw new RuntimeException(
                    "Razorpay payment verification failed",
                    e
            );
        }
    }
    @Override
    public List<PaymentResponseDto> getMyPayments(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        return paymentRepository.findByOrder_User(user)
                .stream()
                .map(payment ->

                        new PaymentResponseDto(

                                payment.getId(),

                                payment.getRazorpayOrderId(),

                                payment.getAmount(),

                                payment.getStatus().name()

                        )
                )
                .toList();
    }
    @Override
    public PaymentResponseDto getPaymentByOrderId(
            String email,
            Long orderId) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        Payment payment = paymentRepository
                .findByOrder_IdAndOrder_User(orderId, user)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Payment not found"));

        return new PaymentResponseDto(

                payment.getId(),

                payment.getRazorpayOrderId(),

                payment.getAmount(),

                payment.getStatus().name()

        );
    }
}