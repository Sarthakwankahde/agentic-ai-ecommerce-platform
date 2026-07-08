package com.sarthak.agenticai.dto;

import com.sarthak.agenticai.entity.DiscountType;

import java.time.LocalDate;

public class CouponResponseDto {

    private Long id;

    private String code;

    private DiscountType discountType;

    private Double discountValue;

    private Double minimumAmount;

    private Double maximumDiscount;

    private LocalDate expiryDate;

    private Boolean active;

    public CouponResponseDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public DiscountType getDiscountType() {
        return discountType;
    }

    public void setDiscountType(DiscountType discountType) {
        this.discountType = discountType;
    }

    public Double getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(Double discountValue) {
        this.discountValue = discountValue;
    }

    public Double getMinimumAmount() {
        return minimumAmount;
    }

    public void setMinimumAmount(Double minimumAmount) {
        this.minimumAmount = minimumAmount;
    }

    public Double getMaximumDiscount() {
        return maximumDiscount;
    }

    public void setMaximumDiscount(Double maximumDiscount) {
        this.maximumDiscount = maximumDiscount;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}