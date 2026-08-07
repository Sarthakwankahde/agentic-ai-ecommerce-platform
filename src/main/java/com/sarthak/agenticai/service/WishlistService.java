package com.sarthak.agenticai.service;

import com.sarthak.agenticai.dto.WishlistResponseDto;

import java.util.List;

public interface WishlistService {

    WishlistResponseDto addToWishlist(
            String email,
            Long productId);

    List<WishlistResponseDto> getWishlist(
            String email);

    void removeFromWishlist(
            String email,
            Long wishlistItemId);

    void clearWishlist(
            String email);
}