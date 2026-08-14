package com.sarthak.agenticai.controller;

import com.sarthak.agenticai.dto.OrderResponseDto;
import com.sarthak.agenticai.service.OrderService;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }
    @PostMapping
    public OrderResponseDto placeOrder(
            Authentication authentication) {

        String email = authentication.getName();

        return orderService.placeOrder(email);
    }
    @GetMapping
    public List<OrderResponseDto> getMyOrders(
            Authentication authentication) {

        String email = authentication.getName();

        return orderService.getMyOrders(email);
    }
    @GetMapping("/{orderId}")
    public OrderResponseDto getOrderById(
            @PathVariable Long orderId,
            Authentication authentication) {

        String email = authentication.getName();

        return orderService.getOrderById(
                orderId,
                email
        );
    }
    @PutMapping("/{orderId}/cancel")
    public void cancelOrder(
            @PathVariable Long orderId,
            Authentication authentication) {

        String email = authentication.getName();

        orderService.cancelOrder(
                orderId,
                email
        );
    }
}