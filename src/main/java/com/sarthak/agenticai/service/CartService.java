package com.sarthak.agenticai.service;

import com.sarthak.agenticai.dto.CartRequestDto;
import com.sarthak.agenticai.dto.CartResponseDto;

import java.util.List;

public interface CartService {

    CartResponseDto addToCart(
            String email,
            CartRequestDto request);

    List<CartResponseDto> getCart(
            String email);

    CartResponseDto updateCartItem(
            String email,
            Long cartItemId,
            Integer quantity);

    void removeCartItem(
            String email,
            Long cartItemId);

    void clearCart(
            String email);
}