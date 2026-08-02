package com.sarthak.agenticai.dto;

public class CategoryRevenueDto {

    private String categoryName;

    private Double revenue;

    public CategoryRevenueDto() {
    }

    public CategoryRevenueDto(String categoryName,
                              Double revenue) {
        this.categoryName = categoryName;
        this.revenue = revenue;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Double getRevenue() {
        return revenue;
    }

    public void setRevenue(Double revenue) {
        this.revenue = revenue;
    }
}