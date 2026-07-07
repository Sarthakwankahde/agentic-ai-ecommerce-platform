package com.sarthak.agenticai.controller;

import com.sarthak.agenticai.dto.CartRequestDto;
import com.sarthak.agenticai.dto.CartResponseDto;
import com.sarthak.agenticai.service.CartService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }
    @PostMapping
    public CartResponseDto addToCart(
            @RequestParam String email,
            @RequestBody @Valid CartRequestDto request) {

        return cartService.addToCart(email, request);
    }
    @GetMapping
    public List<CartResponseDto> getCart(
            @RequestParam String email) {

        return cartService.getCart(email);
    }
    @PutMapping("/{cartItemId}")
    public CartResponseDto updateCartItem(

            @RequestParam String email,

            @PathVariable Long cartItemId,

            @RequestParam Integer quantity) {

        return cartService.updateCartItem(
                email,
                cartItemId,
                quantity);
    }
    @DeleteMapping("/{cartItemId}")
    public void removeCartItem(

            @RequestParam String email,

            @PathVariable Long cartItemId) {

        cartService.removeCartItem(
                email,
                cartItemId);
    }
    @DeleteMapping("/clear")
    public void clearCart(
            @RequestParam String email) {

        cartService.clearCart(email);
    }
}