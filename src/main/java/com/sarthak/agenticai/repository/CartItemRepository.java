package com.sarthak.agenticai.repository;

import com.sarthak.agenticai.entity.Cart;
import com.sarthak.agenticai.entity.CartItem;
import com.sarthak.agenticai.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    Optional<CartItem> findByCartAndProduct(
            Cart cart,
            Product product
    );
}