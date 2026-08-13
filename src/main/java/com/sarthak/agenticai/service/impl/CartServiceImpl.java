package com.sarthak.agenticai.service.impl;


import com.sarthak.agenticai.dto.CartRequestDto;
import com.sarthak.agenticai.dto.CartResponseDto;
import com.sarthak.agenticai.entity.Cart;
import com.sarthak.agenticai.entity.CartItem;
import com.sarthak.agenticai.entity.Product;
import com.sarthak.agenticai.entity.User;
import com.sarthak.agenticai.exception.ResourceNotFoundException;
import com.sarthak.agenticai.repository.CartItemRepository;
import com.sarthak.agenticai.repository.CartRepository;
import com.sarthak.agenticai.repository.ProductRepository;
import com.sarthak.agenticai.repository.UserRepository;
import com.sarthak.agenticai.service.CartService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public CartServiceImpl(
            CartRepository cartRepository,
            CartItemRepository cartItemRepository,
            ProductRepository productRepository,
            UserRepository userRepository) {

        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }
    @Override
    public CartResponseDto addToCart(
            String email,
            CartRequestDto request) {

        // Find Logged-in User
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        // Find Cart or Create New Cart
        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> {

                    Cart newCart = new Cart();
                    newCart.setUser(user);

                    return cartRepository.save(newCart);
                });

        // Find Product
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found"));

        // Check if Product Already Exists in Cart
        CartItem cartItem = cartItemRepository
                .findByCartAndProduct(cart, product)
                .orElse(null);

        if (cartItem != null) {

            // Increase Quantity
            cartItem.setQuantity(
                    cartItem.getQuantity() + request.getQuantity());

        } else {

            // Create New Cart Item
            cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(request.getQuantity());

        }

        // Save Cart Item
        cartItemRepository.save(cartItem);

        // Calculate Total Price
        Double totalPrice =
                product.getPrice() * cartItem.getQuantity();

        // Return Response
        return new CartResponseDto(
                cart.getId(),
                cartItem.getId(),
                product.getId(),
                product.getName(),
                product.getImageUrl(),
                product.getPrice(),
                cartItem.getQuantity(),
                totalPrice
        );
    }
    @Override
    public List<CartResponseDto> getCart(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Cart not found"));

        return cart.getCartItems()
                .stream()
                .map(cartItem -> {

                    Product product = cartItem.getProduct();

                    return new CartResponseDto(
                            cart.getId(),
                            cartItem.getId(),
                            product.getId(),
                            product.getName(),
                            product.getImageUrl(),
                            product.getPrice(),
                            cartItem.getQuantity(),
                            product.getPrice() * cartItem.getQuantity()
                    );
                })
                .toList();
    }
    @Override
    public CartResponseDto updateCartItem(
            String email,
            Long cartItemId,
            Integer quantity) {

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Cart Item not found"));

        cartItem.setQuantity(quantity);

        cartItemRepository.save(cartItem);

        Product product = cartItem.getProduct();

        return new CartResponseDto(
                cartItem.getCart().getId(),
                cartItem.getId(),
                product.getId(),
                product.getName(),
                product.getImageUrl(),
                product.getPrice(),
                cartItem.getQuantity(),
                product.getPrice() * cartItem.getQuantity()
        );
    }
    @Override
    public void removeCartItem(
            String email,
            Long cartItemId) {

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Cart Item not found"));

        cartItemRepository.delete(cartItem);
    }
    @Override
    public void clearCart(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Cart not found"));

        cart.getCartItems().clear();

        cartRepository.save(cart);
    }
}
