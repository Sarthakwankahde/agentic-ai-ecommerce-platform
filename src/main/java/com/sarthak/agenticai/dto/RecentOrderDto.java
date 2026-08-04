package com.sarthak.agenticai.dto;

import com.sarthak.agenticai.entity.OrderStatus;

import java.time.LocalDateTime;

public class RecentOrderDto {

    private Long orderId;

    private String customerName;

    private Double totalAmount;

    private OrderStatus status;

    private LocalDateTime orderDate;

    public RecentOrderDto(Long orderId,
                          String customerName,
                          Double totalAmount,
                          OrderStatus status,
                          LocalDateTime orderDate) {

        this.orderId = orderId;
        this.customerName = customerName;
        this.totalAmount = totalAmount;
        this.status = status;
        this.orderDate = orderDate;
    }

    public Long getOrderId() {
        return orderId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }
}