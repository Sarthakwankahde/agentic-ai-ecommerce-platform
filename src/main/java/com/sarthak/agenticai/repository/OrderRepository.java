package com.sarthak.agenticai.repository;

import com.sarthak.agenticai.dto.BestCustomerDto;
import com.sarthak.agenticai.dto.OrderStatusAnalyticsDto;
import com.sarthak.agenticai.dto.RecentOrderDto;
import com.sarthak.agenticai.dto.SalesTrendDto;
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
    @Query(value = """
SELECT
TO_CHAR(order_date,'Month') AS month,
COALESCE(SUM(total_amount),0) AS revenue,
COUNT(*) AS orders
FROM orders
WHERE status='DELIVERED'
GROUP BY EXTRACT(MONTH FROM order_date),
TO_CHAR(order_date,'Month')
ORDER BY EXTRACT(MONTH FROM order_date)
""", nativeQuery = true)
    List<Object[]> getMonthlySalesAnalytics();
   @Query("""
SELECT new com.sarthak.agenticai.dto.BestCustomerDto(
    u.id,
    u.fullName,
    u.email,
    COUNT(o),
    SUM(o.totalAmount)
)
FROM Order o
JOIN o.user u
WHERE o.status = com.sarthak.agenticai.entity.OrderStatus.DELIVERED
GROUP BY u.id, u.fullName, u.email
ORDER BY SUM(o.totalAmount) DESC
""")
   List<BestCustomerDto> getBestCustomers();
    @Query("""
SELECT new com.sarthak.agenticai.dto.RecentOrderDto(
    o.id,
    o.user.fullName,
    o.totalAmount,
    o.status,
    o.orderDate
)
FROM Order o
ORDER BY o.orderDate DESC
""")
    List<RecentOrderDto> getRecentOrders();
    @Query("""
SELECT new com.sarthak.agenticai.dto.SalesTrendDto(
    'Test',
   CAST(100.0 AS double)
)
FROM Order o
""")
    List<SalesTrendDto> getSalesTrend();
    @Query("""
SELECT new com.sarthak.agenticai.dto.OrderStatusAnalyticsDto(
    o.status,
    COUNT(o)
)
FROM Order o
GROUP BY o.status
ORDER BY o.status
""")
    List<OrderStatusAnalyticsDto> getOrderStatusAnalytics();

}