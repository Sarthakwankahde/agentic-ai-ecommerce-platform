package com.sarthak.agenticai.dto;

public class SalesTrendDto {

    private String month;

    private Double revenue;

    public SalesTrendDto(String month,
                         Double revenue) {

        this.month = month;
        this.revenue = revenue;
    }

    public String getMonth() {
        return month;
    }

    public Double getRevenue() {
        return revenue;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public void setRevenue(Double revenue) {
        this.revenue = revenue;
    }
}