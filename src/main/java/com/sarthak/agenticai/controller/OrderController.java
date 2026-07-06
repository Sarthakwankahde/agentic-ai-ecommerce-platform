package com.sarthak.agenticai.controller;

import com.sarthak.agenticai.dto.OrderResponseDto;
import com.sarthak.agenticai.service.OrderService;
import org.springframework.web.bind.annotation.*;

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
            @RequestParam String email) {

        return orderService.placeOrder(email);
    }

    @GetMapping
    public List<OrderResponseDto> getMyOrders(
            @RequestParam String email) {

        return orderService.getMyOrders(email);
    }

    @GetMapping("/{orderId}")
    public OrderResponseDto getOrderById(

            @PathVariable Long orderId,

            @RequestParam String email) {

        return orderService.getOrderById(
                orderId,
                email);
    }

    @PutMapping("/{orderId}/cancel")
    public void cancelOrder(

            @PathVariable Long orderId,

            @RequestParam String email) {

        orderService.cancelOrder(
                orderId,
                email);
    }
}