package com.sarthak.agenticai.service.impl;

import com.sarthak.agenticai.dto.WishlistRequestDto;
import com.sarthak.agenticai.dto.WishlistResponseDto;
import com.sarthak.agenticai.entity.Product;
import com.sarthak.agenticai.entity.User;
import com.sarthak.agenticai.entity.Wishlist;
import com.sarthak.agenticai.exception.ResourceNotFoundException;
import com.sarthak.agenticai.exception.WishlistAlreadyExistsException;
import com.sarthak.agenticai.repository.ProductRepository;
import com.sarthak.agenticai.repository.UserRepository;
import com.sarthak.agenticai.repository.WishlistRepository;
import com.sarthak.agenticai.service.WishlistService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WishlistServiceImpl implements WishlistService {

    private final WishlistRepository wishlistRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public WishlistServiceImpl(
            WishlistRepository wishlistRepository,
            UserRepository userRepository,
            ProductRepository productRepository) {

        this.wishlistRepository = wishlistRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    @Override
    public WishlistResponseDto addToWishlist(
            String email,
            WishlistRequestDto request) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found"));
        wishlistRepository.findByUserAndProduct(user, product)
                .ifPresent(wishlist -> {
                    throw new WishlistAlreadyExistsException(
                            "Product already exists in wishlist"
                    );
                });

        Wishlist wishlist = new Wishlist();
        wishlist.setUser(user);
        wishlist.setProduct(product);

        Wishlist savedWishlist = wishlistRepository.save(wishlist);

        return new WishlistResponseDto(
                savedWishlist.getId(),
                product.getId(),
                product.getName(),
                product.getImageUrl(),
                product.getPrice()
        );
    }

    @Override
    public List<WishlistResponseDto> getWishlist(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        return wishlistRepository.findByUser(user)
                .stream()
                .map(wishlist -> {

                    Product product = wishlist.getProduct();

                    return new WishlistResponseDto(
                            wishlist.getId(),
                            product.getId(),
                            product.getName(),
                            product.getImageUrl(),
                            product.getPrice()
                    );
                })
                .toList();
    }

    @Override
    public void removeFromWishlist(
            String email,
            Long wishlistId) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        Wishlist wishlist = wishlistRepository.findById(wishlistId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Wishlist item not found"));

        if (!wishlist.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You cannot remove another user's wishlist item");
        }

        wishlistRepository.delete(wishlist);
    }
}