package com.sarthak.agenticai.controller;

import com.sarthak.agenticai.dto.WishlistRequestDto;
import com.sarthak.agenticai.dto.WishlistResponseDto;
import com.sarthak.agenticai.service.WishlistService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/wishlist")
public class WishlistController {

    private final WishlistService wishlistService;

    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    // ========================================
    // ADD PRODUCT TO WISHLIST
    // ========================================

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WishlistResponseDto addToWishlist(
            @RequestParam String email,
            @Valid @RequestBody WishlistRequestDto request) {

        return wishlistService.addToWishlist(
                email,
                request.getProductId()
        );
    }

    // ========================================
    // GET USER WISHLIST
    // ========================================

    @GetMapping
    public List<WishlistResponseDto> getWishlist(
            @RequestParam String email) {

        return wishlistService.getWishlist(email);
    }

    // ========================================
    // REMOVE ITEM FROM WISHLIST
    // ========================================

    @DeleteMapping("/{wishlistItemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeFromWishlist(
            @RequestParam String email,
            @PathVariable Long wishlistItemId) {

        wishlistService.removeFromWishlist(
                email,
                wishlistItemId
        );
    }

    // ========================================
    // CLEAR WISHLIST
    // ========================================

    @DeleteMapping("/clear")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void clearWishlist(
            @RequestParam String email) {

        wishlistService.clearWishlist(email);
    }
}