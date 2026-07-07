package com.sarthak.agenticai.dto;

public class PaymentResponseDto {

    private Long paymentId;

    private String razorpayOrderId;

    private Double amount;

    private String status;

    public PaymentResponseDto() {
    }

    public PaymentResponseDto(
            Long paymentId,
            String razorpayOrderId,
            Double amount,
            String status) {

        this.paymentId = paymentId;
        this.razorpayOrderId = razorpayOrderId;
        this.amount = amount;
        this.status = status;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public String getRazorpayOrderId() {
        return razorpayOrderId;
    }

    public void setRazorpayOrderId(String razorpayOrderId) {
        this.razorpayOrderId = razorpayOrderId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}