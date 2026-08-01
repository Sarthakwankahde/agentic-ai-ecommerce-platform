package com.sarthak.agenticai.dto;

public class RevenueAnalyticsDto {

    private Double totalRevenue;

    private Double todayRevenue;

    private Double monthlyRevenue;

    private Double yearlyRevenue;

    public RevenueAnalyticsDto() {
    }

    public RevenueAnalyticsDto(Double totalRevenue,
                               Double todayRevenue,
                               Double monthlyRevenue,
                               Double yearlyRevenue) {
        this.totalRevenue = totalRevenue;
        this.todayRevenue = todayRevenue;
        this.monthlyRevenue = monthlyRevenue;
        this.yearlyRevenue = yearlyRevenue;
    }

    public Double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(Double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public Double getTodayRevenue() {
        return todayRevenue;
    }

    public void setTodayRevenue(Double todayRevenue) {
        this.todayRevenue = todayRevenue;
    }

    public Double getMonthlyRevenue() {
        return monthlyRevenue;
    }

    public void setMonthlyRevenue(Double monthlyRevenue) {
        this.monthlyRevenue = monthlyRevenue;
    }

    public Double getYearlyRevenue() {
        return yearlyRevenue;
    }

    public void setYearlyRevenue(Double yearlyRevenue) {
        this.yearlyRevenue = yearlyRevenue;
    }
}