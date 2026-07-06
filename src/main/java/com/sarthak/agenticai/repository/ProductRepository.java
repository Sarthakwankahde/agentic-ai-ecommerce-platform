package com.sarthak.agenticai.repository;

import com.sarthak.agenticai.dto.ProductSummaryDto;
import com.sarthak.agenticai.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import com.sarthak.agenticai.projection.ProductSummary;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository
        extends JpaRepository<Product, Long>,
        JpaSpecificationExecutor<Product> {

    List<Product> findByNameContainingIgnoreCase(String keyword);

    List<Product> findByCategoryId(Long categoryId);

    List<Product> findByPriceBetween(
            Double minPrice,
            Double maxPrice);
    List<ProductSummary> findAllBy();
    @Query("""
       SELECT new com.sarthak.agenticai.dto.ProductSummaryDto(
              p.name,
              p.price
       )
       FROM Product p
       """)
    List<ProductSummaryDto> getProductSummaryDto();
}