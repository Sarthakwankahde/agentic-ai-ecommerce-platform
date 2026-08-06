package com.sarthak.agenticai.ai.tool;

import com.sarthak.agenticai.dto.CartRequestDto;
import com.sarthak.agenticai.dto.CartResponseDto;
import com.sarthak.agenticai.service.CartService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CartTool {

    private final CartService cartService;

    public CartTool(CartService cartService) {
        this.cartService = cartService;
    }

    public String getCart(String email) {

        return buildCartResponse(
                cartService.getCart(email)
        );
    }

    public String addToCart(
            String email,
            Long productId,
            Integer quantity) {

        CartRequestDto request = new CartRequestDto();

        request.setProductId(productId);
        request.setQuantity(quantity);

        CartResponseDto cart =
                cartService.addToCart(email, request);

        return buildCartResponse(List.of(cart));
    }

    public String updateCartItem(
            String email,
            Long cartItemId,
            Integer quantity) {

        CartResponseDto cart =
                cartService.updateCartItem(
                        email,
                        cartItemId,
                        quantity
                );

        return buildCartResponse(List.of(cart));
    }

    public String removeCartItem(
            String email,
            Long cartItemId) {

        cartService.removeCartItem(email, cartItemId);

        return "Cart item removed successfully.";
    }

    public String clearCart(String email) {

        cartService.clearCart(email);

        return "Your cart has been cleared.";
    }

    private String buildCartResponse(List<CartResponseDto> carts) {

        if (carts.isEmpty()) {
            return "Your cart is empty.";
        }

        StringBuilder response = new StringBuilder();

        int index = 1;

        for (CartResponseDto cart : carts) {

            response.append("""
Cart Item %d

Product : %s
Price : ₹%s
Quantity : %d
Total : ₹%s

""".formatted(
                    index++,
                    cart.getProductName(),
                    cart.getPrice(),
                    cart.getQuantity(),
                    cart.getTotalPrice()
            ));
        }

        return response.toString();
    }
}