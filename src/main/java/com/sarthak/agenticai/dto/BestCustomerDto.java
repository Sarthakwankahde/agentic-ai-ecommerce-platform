package com.sarthak.agenticai.dto;

public class BestCustomerDto {

    private Long userId;

    private String fullName;

    private String email;

    private Long totalOrders;

    private Double totalSpent;

    public BestCustomerDto(Long userId,
                           String fullName,
                           String email,
                           Long totalOrders,
                           Double totalSpent) {

        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.totalOrders = totalOrders;
        this.totalSpent = totalSpent;
    }

    public Long getUserId() {
        return userId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public Long getTotalOrders() {
        return totalOrders;
    }

    public Double getTotalSpent() {
        return totalSpent;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTotalOrders(Long totalOrders) {
        this.totalOrders = totalOrders;
    }

    public void setTotalSpent(Double totalSpent) {
        this.totalSpent = totalSpent;
    }
}