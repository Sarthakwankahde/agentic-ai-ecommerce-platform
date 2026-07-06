package com.sarthak.agenticai.dto;

public class ProductSummaryDto {

    private String name;
    private Double price;

    public ProductSummaryDto(String name, Double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public Double getPrice() {
        return price;
    }
}