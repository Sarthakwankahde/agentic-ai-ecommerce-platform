package com.sarthak.agenticai.exception;

public class CouponExpiredException extends RuntimeException {

    public CouponExpiredException(String message) {
        super(message);
    }
}