package com.sarthak.agenticai.dto;

import java.time.LocalDateTime;

public class ReviewResponseDto {

    private Long reviewId;

    private Long productId;

    private String productName;

    private String userName;

    private Integer rating;

    private String comment;

    private LocalDateTime createdAt;

    public ReviewResponseDto() {
    }

    public ReviewResponseDto(Long reviewId,
                             Long productId,
                             String productName,
                             String userName,
                             Integer rating,
                             String comment,
                             LocalDateTime createdAt) {

        this.reviewId = reviewId;
        this.productId = productId;
        this.productName = productName;
        this.userName = userName;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = createdAt;
    }

    public Long getReviewId() {
        return reviewId;
    }

    public void setReviewId(Long reviewId) {
        this.reviewId = reviewId;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}