package com.sarthak.agenticai.repository;

import com.sarthak.agenticai.entity.OrderItem;
import com.sarthak.agenticai.entity.Product;
import com.sarthak.agenticai.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    boolean existsByOrderUserAndProduct(User user, Product product);

}