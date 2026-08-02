package com.sarthak.agenticai.dto;

public class MonthlySalesDto {

    private String month;

    private Double revenue;

    private Long orders;

    public MonthlySalesDto() {
    }

    public MonthlySalesDto(String month,
                           Double revenue,
                           Long orders) {

        this.month = month;
        this.revenue = revenue;
        this.orders = orders;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public Double getRevenue() {
        return revenue;
    }

    public void setRevenue(Double revenue) {
        this.revenue = revenue;
    }

    public Long getOrders() {
        return orders;
    }

    public void setOrders(Long orders) {
        this.orders = orders;
    }
}