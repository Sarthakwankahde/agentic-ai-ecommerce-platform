package com.sarthak.agenticai.repository;

import com.sarthak.agenticai.entity.Product;
import com.sarthak.agenticai.entity.User;
import com.sarthak.agenticai.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WishlistRepository
        extends JpaRepository<Wishlist, Long> {

    // Get all wishlist items of a user
    List<Wishlist> findByUser(User user);

    // Check whether a product already exists in user's wishlist
    Optional<Wishlist> findByUserAndProduct(
            User user,
            Product product
    );
}