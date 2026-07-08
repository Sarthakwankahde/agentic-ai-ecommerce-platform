package com.sarthak.agenticai.repository;

import com.sarthak.agenticai.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository
        extends JpaRepository<OrderItem, Long> {

}