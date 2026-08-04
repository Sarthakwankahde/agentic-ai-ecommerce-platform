package com.sarthak.agenticai.dto;

public class CustomerGrowthDto {

    private String month;

    private Long totalCustomers;
    public CustomerGrowthDto(String month, Long totalCustomers) {
        this.month = month;
        this.totalCustomers = totalCustomers;
    }


    public String getMonth() {
        return month;
    }


    public Long getTotalCustomers() {
        return totalCustomers;
    }


    public void setMonth(String month) {
        this.month = month;
    }


    public void setTotalCustomers(Long totalCustomers) {
        this.totalCustomers = totalCustomers;
    }
}