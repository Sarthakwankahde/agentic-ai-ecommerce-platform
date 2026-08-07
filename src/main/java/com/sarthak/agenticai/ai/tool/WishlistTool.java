package com.sarthak.agenticai.ai.tool;

import com.sarthak.agenticai.dto.WishlistResponseDto;
import com.sarthak.agenticai.service.WishlistService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WishlistTool {

    private final WishlistService wishlistService;

    public WishlistTool(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    private String buildWishlistResponse(List<WishlistResponseDto> items) {

        if (items.isEmpty()) {
            return "Your wishlist is empty.";
        }

        StringBuilder response = new StringBuilder();

        int index = 1;

        for (WishlistResponseDto item : items) {

            response.append("""
                    
Wishlist Item %d

Product : %s
Category : %s
Price : ₹%s

""".formatted(
                    index++,
                    item.getProductName(),
                    item.getCategoryName(),
                    item.getPrice()
            ));
        }

        return response.toString();
    }

    @Tool(description = "Returns all products in the user's wishlist")
    public String getWishlist(String email) {

        return buildWishlistResponse(
                wishlistService.getWishlist(email)
        );
    }

    @Tool(description = "Adds a product to the wishlist")
    public String addToWishlist(
            String email,
            Long productId) {

        WishlistResponseDto item =
                wishlistService.addToWishlist(email, productId);

        return buildWishlistResponse(List.of(item));
    }

    @Tool(description = "Removes a product from the wishlist")
    public String removeFromWishlist(
            String email,
            Long wishlistItemId) {

        wishlistService.removeFromWishlist(email, wishlistItemId);

        return "Product removed from wishlist successfully.";
    }

    @Tool(description = "Clears the entire wishlist")
    public String clearWishlist(String email) {

        wishlistService.clearWishlist(email);

        return "Wishlist cleared successfully.";
    }
}