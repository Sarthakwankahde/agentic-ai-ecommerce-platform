package com.sarthak.agenticai.dto;

import jakarta.validation.constraints.NotNull;

public class PaymentRequestDto {

    @NotNull(message = "Order Id is required")
    private Long orderId;

    public PaymentRequestDto() {
    }

    public PaymentRequestDto(Long orderId) {
        this.orderId = orderId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
}