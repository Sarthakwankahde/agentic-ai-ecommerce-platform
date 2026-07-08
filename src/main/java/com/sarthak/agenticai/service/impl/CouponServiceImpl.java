package com.sarthak.agenticai.service.impl;

import com.sarthak.agenticai.dto.CouponRequestDto;
import com.sarthak.agenticai.dto.CouponResponseDto;
import com.sarthak.agenticai.entity.Coupon;
import com.sarthak.agenticai.exception.CouponAlreadyExistsException;
import com.sarthak.agenticai.exception.CouponExpiredException;
import com.sarthak.agenticai.exception.CouponInactiveException;
import com.sarthak.agenticai.exception.ResourceNotFoundException;
import com.sarthak.agenticai.repository.CouponRepository;
import com.sarthak.agenticai.service.CouponService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;

    public CouponServiceImpl(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }
    @Override
    public CouponResponseDto createCoupon(
            CouponRequestDto request) {

        if (couponRepository.existsByCode(request.getCode())) {
            throw new CouponAlreadyExistsException("Coupon code already exists");
        }

        Coupon coupon = new Coupon();

        coupon.setCode(request.getCode());
        coupon.setDiscountType(request.getDiscountType());
        coupon.setDiscountValue(request.getDiscountValue());
        coupon.setMinimumAmount(request.getMinimumAmount());
        coupon.setMaximumDiscount(request.getMaximumDiscount());
        coupon.setExpiryDate(request.getExpiryDate());
        coupon.setActive(request.getActive());

        Coupon savedCoupon =
                couponRepository.save(coupon);

        CouponResponseDto response =
                new CouponResponseDto();

        response.setId(savedCoupon.getId());
        response.setCode(savedCoupon.getCode());
        response.setDiscountType(savedCoupon.getDiscountType());
        response.setDiscountValue(savedCoupon.getDiscountValue());
        response.setMinimumAmount(savedCoupon.getMinimumAmount());
        response.setMaximumDiscount(savedCoupon.getMaximumDiscount());
        response.setExpiryDate(savedCoupon.getExpiryDate());
        response.setActive(savedCoupon.getActive());

        return response;
    }
    @Override
    public CouponResponseDto updateCoupon(
            Long couponId,
            CouponRequestDto request) {

        // Find existing coupon
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Coupon not found"));

        // Check duplicate coupon code only if the code is changed
        if (!coupon.getCode().equalsIgnoreCase(request.getCode())
                && couponRepository.existsByCode(request.getCode())) {
            throw new CouponAlreadyExistsException("Coupon code already exists");
        }

        // Update coupon details
        coupon.setCode(request.getCode());
        coupon.setDiscountType(request.getDiscountType());
        coupon.setDiscountValue(request.getDiscountValue());
        coupon.setMinimumAmount(request.getMinimumAmount());
        coupon.setMaximumDiscount(request.getMaximumDiscount());
        coupon.setExpiryDate(request.getExpiryDate());
        coupon.setActive(request.getActive());

        // Save updated coupon
        Coupon updatedCoupon = couponRepository.save(coupon);

        // Convert Entity -> DTO
        CouponResponseDto response = new CouponResponseDto();

        response.setId(updatedCoupon.getId());
        response.setCode(updatedCoupon.getCode());
        response.setDiscountType(updatedCoupon.getDiscountType());
        response.setDiscountValue(updatedCoupon.getDiscountValue());
        response.setMinimumAmount(updatedCoupon.getMinimumAmount());
        response.setMaximumDiscount(updatedCoupon.getMaximumDiscount());
        response.setExpiryDate(updatedCoupon.getExpiryDate());
        response.setActive(updatedCoupon.getActive());

        return response;
    }
    @Override
    public void deleteCoupon(Long couponId) {

        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Coupon not found"));

        couponRepository.delete(coupon);
    }
    @Override
    public CouponResponseDto getCouponById(Long couponId) {

        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Coupon not found"));

        CouponResponseDto response = new CouponResponseDto();

        response.setId(coupon.getId());
        response.setCode(coupon.getCode());
        response.setDiscountType(coupon.getDiscountType());
        response.setDiscountValue(coupon.getDiscountValue());
        response.setMinimumAmount(coupon.getMinimumAmount());
        response.setMaximumDiscount(coupon.getMaximumDiscount());
        response.setExpiryDate(coupon.getExpiryDate());
        response.setActive(coupon.getActive());

        return response;
    }
    @Override
    public List<CouponResponseDto> getAllCoupons() {

        return couponRepository.findAll()
                .stream()
                .map(coupon -> {

                    CouponResponseDto response =
                            new CouponResponseDto();

                    response.setId(coupon.getId());
                    response.setCode(coupon.getCode());
                    response.setDiscountType(coupon.getDiscountType());
                    response.setDiscountValue(coupon.getDiscountValue());
                    response.setMinimumAmount(coupon.getMinimumAmount());
                    response.setMaximumDiscount(coupon.getMaximumDiscount());
                    response.setExpiryDate(coupon.getExpiryDate());
                    response.setActive(coupon.getActive());

                    return response;

                })
                .toList();
    }
    @Override
    public CouponResponseDto validateCoupon(String couponCode) {

        Coupon coupon = couponRepository.findByCode(couponCode)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Coupon not found"));

        // Check Active Status
        if (!coupon.getActive()) {
            throw new CouponInactiveException("Coupon is inactive");
        }

        // Check Expiry
        if (coupon.getExpiryDate().isBefore(LocalDate.now())) {
            throw new CouponExpiredException("Coupon has expired");
        }

        CouponResponseDto response = new CouponResponseDto();

        response.setId(coupon.getId());
        response.setCode(coupon.getCode());
        response.setDiscountType(coupon.getDiscountType());
        response.setDiscountValue(coupon.getDiscountValue());
        response.setMinimumAmount(coupon.getMinimumAmount());
        response.setMaximumDiscount(coupon.getMaximumDiscount());
        response.setExpiryDate(coupon.getExpiryDate());
        response.setActive(coupon.getActive());

        return response;
    }
    @Override
    public Double calculateDiscount(
            String couponCode,
            Double orderAmount) {

        Coupon coupon = couponRepository.findByCode(couponCode)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Coupon not found"));

        // Check Active
        if (!coupon.getActive()) {
            throw new CouponInactiveException("Coupon is inactive");
        }

        // Check Expiry
        if (coupon.getExpiryDate().isBefore(LocalDate.now())) {
            throw new CouponExpiredException("Coupon has expired");
        }

        // Check Minimum Order Amount
        if (orderAmount < coupon.getMinimumAmount()) {
            throw new RuntimeException(
                    "Minimum order amount should be ₹"
                            + coupon.getMinimumAmount());
        }

        double discount;

        // Percentage Discount
        if (coupon.getDiscountType().name().equals("PERCENTAGE")) {

            discount =
                    orderAmount * coupon.getDiscountValue() / 100;

            // Apply Maximum Discount
            if (discount > coupon.getMaximumDiscount()) {
                discount = coupon.getMaximumDiscount();
            }

        } else {

            // Fixed Discount
            discount = coupon.getDiscountValue();
        }

        return discount;
    }

}