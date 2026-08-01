package com.sarthak.agenticai.repository;

import com.sarthak.agenticai.entity.Order;
import com.sarthak.agenticai.entity.OrderStatus;
import com.sarthak.agenticai.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUser(User user);

    Optional<Order> findByIdAndUser(
            Long orderId,
            User user
    );
    long countByStatus(OrderStatus status);

    @Query("""
        SELECT COALESCE(SUM(o.totalAmount),0)
        FROM Order o
        WHERE o.status='DELIVERED'
        """)
    Double getTotalRevenue();
    @Query("""
SELECT COALESCE(SUM(o.totalAmount),0)
FROM Order o
WHERE o.status='DELIVERED'
AND o.orderDate BETWEEN :startDate AND :endDate
""")
    Double getRevenueBetweenDates(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
    @Query("""
SELECT COUNT(o)
FROM Order o
WHERE o.orderDate BETWEEN :startDate AND :endDate
""")
    Long countOrdersBetweenDates(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
}