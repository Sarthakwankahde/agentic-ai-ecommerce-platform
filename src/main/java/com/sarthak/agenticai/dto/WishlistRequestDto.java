package com.sarthak.agenticai.dto;

import jakarta.validation.constraints.NotNull;

public class WishlistRequestDto {

    @NotNull(message = "Product Id is required")
    private Long productId;

    public WishlistRequestDto() {
    }

    public WishlistRequestDto(Long productId) {
        this.productId = productId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
}