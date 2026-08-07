package com.sarthak.agenticai.dto;

public class WishlistResponseDto {

    private Long wishlistId;

    private Long productId;

    private String productName;

    private String imageUrl;

    private Double price;

    private String categoryName;

    public WishlistResponseDto() {
    }

    public WishlistResponseDto(
            Long wishlistId,
            Long productId,
            String productName,
            String imageUrl,
            Double price,
            String categoryName) {

        this.wishlistId = wishlistId;
        this.productId = productId;
        this.productName = productName;
        this.imageUrl = imageUrl;
        this.price = price;
        this.categoryName = categoryName;
    }

    public Long getWishlistId() {
        return wishlistId;
    }

    public void setWishlistId(Long wishlistId) {
        this.wishlistId = wishlistId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}