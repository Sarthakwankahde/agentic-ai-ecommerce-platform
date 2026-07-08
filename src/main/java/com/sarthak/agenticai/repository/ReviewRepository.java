package com.sarthak.agenticai.repository;

import com.sarthak.agenticai.entity.Product;
import com.sarthak.agenticai.entity.Review;
import com.sarthak.agenticai.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query("""
       SELECT AVG(r.rating)
       FROM Review r
       WHERE r.product = :product
       """)
    Double getAverageRating(Product product);

    @Query("""
       SELECT COUNT(r)
       FROM Review r
       WHERE r.product = :product
       """)
    Long getReviewCount(Product product);

    // Get all reviews of a product
    List<Review> findByProduct(Product product);

    // Check whether a user has already reviewed a product
    Optional<Review> findByUserAndProduct(
            User user,
            Product product
    );
}