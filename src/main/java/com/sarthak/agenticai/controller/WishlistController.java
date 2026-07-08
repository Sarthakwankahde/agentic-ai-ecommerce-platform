package com.sarthak.agenticai.controller;

import com.sarthak.agenticai.dto.WishlistRequestDto;
import com.sarthak.agenticai.dto.WishlistResponseDto;
import com.sarthak.agenticai.service.WishlistService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/wishlist")
public class WishlistController {

    private final WishlistService wishlistService;

    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    // Add Product to Wishlist
    @PostMapping
    public WishlistResponseDto addToWishlist(

            @RequestParam String email,

            @RequestBody @Valid WishlistRequestDto request) {

        return wishlistService.addToWishlist(
                email,
                request
        );
    }

    // Get Wishlist
    @GetMapping
    public List<WishlistResponseDto> getWishlist(

            @RequestParam String email) {

        return wishlistService.getWishlist(email);
    }

    // Remove Product
    @DeleteMapping("/{wishlistId}")
    public void removeFromWishlist(

            @RequestParam String email,

            @PathVariable Long wishlistId) {

        wishlistService.removeFromWishlist(
                email,
                wishlistId
        );
    }
}