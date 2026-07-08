package com.sarthak.agenticai.service;

import com.sarthak.agenticai.dto.WishlistRequestDto;
import com.sarthak.agenticai.dto.WishlistResponseDto;

import java.util.List;

public interface WishlistService {

    // Add Product to Wishlist
    WishlistResponseDto addToWishlist(
            String email,
            WishlistRequestDto request);

    // Get User Wishlist
    List<WishlistResponseDto> getWishlist(
            String email);

    // Remove Product from Wishlist
    void removeFromWishlist(
            String email,
            Long wishlistId);
}