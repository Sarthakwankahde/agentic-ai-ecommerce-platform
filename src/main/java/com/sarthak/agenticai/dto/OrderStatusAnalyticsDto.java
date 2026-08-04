package com.sarthak.agenticai.dto;

import com.sarthak.agenticai.entity.OrderStatus;

public class OrderStatusAnalyticsDto {

    private OrderStatus status;

    private Long totalOrders;

    public OrderStatusAnalyticsDto(OrderStatus status,
                                   Long totalOrders) {

        this.status = status;
        this.totalOrders = totalOrders;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public Long getTotalOrders() {
        return totalOrders;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public void setTotalOrders(Long totalOrders) {
        this.totalOrders = totalOrders;
    }
}