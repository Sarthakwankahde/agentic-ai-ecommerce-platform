package com.sarthak.agenticai.service;

import com.sarthak.agenticai.dto.OrderResponseDto;

import java.util.List;

public interface OrderService {

    OrderResponseDto placeOrder(String email);

    List<OrderResponseDto> getMyOrders(String email);

    OrderResponseDto getOrderById(
            Long orderId,
            String email);

    void cancelOrder(
            Long orderId,
            String email);
}