package com.sarthak.agenticai.repository;

import com.sarthak.agenticai.dto.CategoryRevenueDto;
import com.sarthak.agenticai.dto.TopSellingProductDto;
import com.sarthak.agenticai.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderItemRepository
        extends JpaRepository<OrderItem, Long> {

    @Query("""
            SELECT new com.sarthak.agenticai.dto.TopSellingProductDto(
                    oi.product.id,
                    oi.product.name,
                    SUM(oi.quantity)
            )
            FROM OrderItem oi
            GROUP BY oi.product.id, oi.product.name
            ORDER BY SUM(oi.quantity) DESC
            """)
    List<TopSellingProductDto> getTopSellingProducts();
    @Query("""
SELECT new com.sarthak.agenticai.dto.CategoryRevenueDto(
        oi.product.category.name,
        SUM(oi.price * oi.quantity)
)
FROM OrderItem oi
WHERE oi.order.status = 'DELIVERED'
GROUP BY oi.product.category.name
ORDER BY SUM(oi.price * oi.quantity) DESC
""")
    List<CategoryRevenueDto> getCategoryRevenue();
}