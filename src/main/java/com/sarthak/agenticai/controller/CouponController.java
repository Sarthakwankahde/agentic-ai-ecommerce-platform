package com.sarthak.agenticai.controller;

import com.sarthak.agenticai.dto.CouponRequestDto;
import com.sarthak.agenticai.dto.CouponResponseDto;
import com.sarthak.agenticai.service.CouponService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/coupons")
public class CouponController {

    private final CouponService couponService;

    public CouponController(CouponService couponService) {
        this.couponService = couponService;
    }

    // Create Coupon
    @PostMapping
    public CouponResponseDto createCoupon(
            @RequestBody CouponRequestDto request) {

        return couponService.createCoupon(request);
    }

    // Update Coupon
    @PutMapping("/{couponId}")
    public CouponResponseDto updateCoupon(
            @PathVariable Long couponId,
            @RequestBody CouponRequestDto request) {

        return couponService.updateCoupon(couponId, request);
    }

    // Delete Coupon
    @DeleteMapping("/{couponId}")
    public void deleteCoupon(
            @PathVariable Long couponId) {

        couponService.deleteCoupon(couponId);
    }

    // Get Coupon By Id
    @GetMapping("/{couponId}")
    public CouponResponseDto getCouponById(
            @PathVariable Long couponId) {

        return couponService.getCouponById(couponId);
    }

    // Get All Coupons
    @GetMapping
    public List<CouponResponseDto> getAllCoupons() {

        return couponService.getAllCoupons();
    }

    // Validate Coupon
    @GetMapping("/validate")
    public CouponResponseDto validateCoupon(
            @RequestParam String couponCode) {

        return couponService.validateCoupon(couponCode);
    }

    // Calculate Discount
    @GetMapping("/calculate-discount")
    public Double calculateDiscount(
            @RequestParam String couponCode,
            @RequestParam Double orderAmount) {

        return couponService.calculateDiscount(
                couponCode,
                orderAmount);
    }
}