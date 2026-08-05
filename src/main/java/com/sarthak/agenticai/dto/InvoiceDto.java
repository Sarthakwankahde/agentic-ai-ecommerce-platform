package com.sarthak.agenticai.dto;

import java.time.LocalDateTime;
import java.util.List;

public class InvoiceDto {

    private Long orderId;

    private String customerName;

    private String email;

    private String address;

    private LocalDateTime orderDate;

    private Double totalAmount;

    private List<InvoiceItemDto> items;

    public InvoiceDto() {
    }

    public InvoiceDto(Long orderId,
                      String customerName,
                      String email,
                      String address,
                      LocalDateTime orderDate,
                      Double totalAmount,
                      List<InvoiceItemDto> items) {

        this.orderId = orderId;
        this.customerName = customerName;
        this.email = email;
        this.address = address;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.items = items;
    }

    public Long getOrderId() {
        return orderId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public List<InvoiceItemDto> getItems() {
        return items;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void setItems(List<InvoiceItemDto> items) {
        this.items = items;
    }
}