package com.sarthak.agenticai.dto;

public class InvoiceItemDto {

    private String productName;

    private Integer quantity;

    private Double price;

    public InvoiceItemDto() {
    }

    public InvoiceItemDto(String productName,
                          Integer quantity,
                          Double price) {

        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
    }

    public String getProductName() {
        return productName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}