package com.sarthak.agenticai.service;

import com.sarthak.agenticai.dto.CouponRequestDto;
import com.sarthak.agenticai.dto.CouponResponseDto;

import java.util.List;

public interface CouponService {

    // Admin
    CouponResponseDto createCoupon(CouponRequestDto request);

    // Admin
    CouponResponseDto updateCoupon(
            Long couponId,
            CouponRequestDto request);

    // Admin
    void deleteCoupon(Long couponId);

    // Admin + User
    CouponResponseDto getCouponById(Long couponId);

    // Admin
    List<CouponResponseDto> getAllCoupons();

    // User
    CouponResponseDto validateCoupon(String couponCode);

    // User
    Double calculateDiscount(
            String couponCode,
            Double orderAmount);
}