package com.sarthak.agenticai.ai.tool;

import com.sarthak.agenticai.dto.CartRequestDto;
import com.sarthak.agenticai.dto.CartResponseDto;
import com.sarthak.agenticai.service.CartService;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CartTool {

    private final CartService cartService;

    public CartTool(CartService cartService) {
        this.cartService = cartService;
    }

    @Tool(description = "Returns all products currently present in the customer's shopping cart.")
    public String getCart(ToolContext toolContext) {

        String email = (String) toolContext
                .getContext()
                .get("email");

        return buildCartResponse(
                cartService.getCart(email)
        );
    }

    @Tool(description = "Adds a product to the customer's shopping cart.")
    public String addToCart(
            Long productId,
            Integer quantity,
            ToolContext toolContext) {

        String email = (String) toolContext
                .getContext()
                .get("email");

        CartRequestDto request = new CartRequestDto();

        request.setProductId(productId);
        request.setQuantity(quantity);

        CartResponseDto cart =
                cartService.addToCart(email, request);

        return buildCartResponse(List.of(cart));
    }

    @Tool(description = "Updates the quantity of an existing product in the customer's shopping cart.")
    public String updateCartItem(
            Long cartItemId,
            Integer quantity,
            ToolContext toolContext) {

        String email = (String) toolContext
                .getContext()
                .get("email");

        CartResponseDto cart =
                cartService.updateCartItem(
                        email,
                        cartItemId,
                        quantity
                );

        return buildCartResponse(List.of(cart));
    }

    @Tool(description = "Removes a specific product from the customer's shopping cart.")
    public String removeCartItem(
            Long cartItemId,
            ToolContext toolContext) {

        String email = (String) toolContext
                .getContext()
                .get("email");

        cartService.removeCartItem(
                email,
                cartItemId
        );

        return "Cart item removed successfully.";
    }

    @Tool(description = "Removes every product from the customer's shopping cart.")
    public String clearCart(ToolContext toolContext) {

        String email = (String) toolContext
                .getContext()
                .get("email");

        cartService.clearCart(email);

        return "Your cart has been cleared.";
    }

    private String buildCartResponse(
            List<CartResponseDto> carts) {

        if (carts.isEmpty()) {
            return "Your cart is empty.";
        }

        StringBuilder response = new StringBuilder();

        int index = 1;

        double grandTotal = 0;
        int totalItems = 0;

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

            grandTotal += cart.getTotalPrice();
            totalItems += cart.getQuantity();
        }

        response.append("""
                ------------------------------
                Total Items : %d
                Grand Total : ₹%s
                """.formatted(
                totalItems,
                grandTotal
        ));

        return response.toString();
    }
}