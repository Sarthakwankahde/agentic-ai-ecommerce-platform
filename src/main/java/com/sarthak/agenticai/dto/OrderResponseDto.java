package com.sarthak.agenticai.dto;

import com.sarthak.agenticai.entity.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

public class OrderResponseDto {

    private Long orderId;

    private Double totalAmount;

    private LocalDateTime orderDate;

    private OrderStatus status;

    private List<OrderItemResponseDto> items;

    public OrderResponseDto() {
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public List<OrderItemResponseDto> getItems() {
        return items;
    }

    public void setItems(List<OrderItemResponseDto> items) {
        this.items = items;
    }
}