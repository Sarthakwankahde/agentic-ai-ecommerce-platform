package com.sarthak.agenticai.service.impl;

import com.sarthak.agenticai.dto.ReviewRequestDto;
import com.sarthak.agenticai.dto.ReviewResponseDto;
import com.sarthak.agenticai.entity.Product;
import com.sarthak.agenticai.entity.Review;
import com.sarthak.agenticai.entity.User;
import com.sarthak.agenticai.exception.ResourceNotFoundException;
import com.sarthak.agenticai.repository.OrderItemRepository;
import com.sarthak.agenticai.repository.ProductRepository;
import com.sarthak.agenticai.repository.ReviewRepository;
import com.sarthak.agenticai.repository.UserRepository;
import com.sarthak.agenticai.service.ReviewService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final OrderItemRepository orderItemRepository;

    public ReviewServiceImpl(
            ReviewRepository reviewRepository,
            ProductRepository productRepository,
            UserRepository userRepository,
            OrderItemRepository orderItemRepository) {

        this.reviewRepository = reviewRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.orderItemRepository = orderItemRepository;
    }
    @Override
    public ReviewResponseDto addReview(
            String email,
            ReviewRequestDto request) {

        // Find User
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        // Find Product
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found"));

        // Check whether user purchased the product
        boolean purchased =
                orderItemRepository.existsByOrderUserAndProduct(
                        user,
                        product
                );

        if (!purchased) {
            throw new RuntimeException(
                    "You can review only purchased products");
        }

        // Check duplicate review
        reviewRepository.findByUserAndProduct(user, product)
                .ifPresent(review -> {
                    throw new RuntimeException(
                            "You have already reviewed this product");
                });

        // Create Review
        Review review = new Review();

        review.setUser(user);
        review.setProduct(product);
        review.setRating(request.getRating());
        review.setComment(request.getComment());
        review.setCreatedAt(java.time.LocalDateTime.now());

        // Save Review
        Review savedReview = reviewRepository.save(review);

        // Prepare Response
        ReviewResponseDto response = new ReviewResponseDto();

        response.setReviewId(savedReview.getId());
        response.setProductId(product.getId());
        response.setProductName(product.getName());
        response.setUserName(user.getFullName());
        response.setRating(savedReview.getRating());
        response.setComment(savedReview.getComment());
        response.setCreatedAt(savedReview.getCreatedAt());

        return response;
    }
    @Override
    public List<ReviewResponseDto> getReviewsByProduct(Long productId) {

        // Check Product Exists
        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found"));

        return reviewRepository.findByProduct(product)
                .stream()
                .map(review -> {

                    ReviewResponseDto response = new ReviewResponseDto();

                    response.setReviewId(review.getId());
                    response.setProductId(product.getId());
                    response.setProductName(product.getName());
                    response.setUserName(review.getUser().getFullName());
                    response.setRating(review.getRating());
                    response.setComment(review.getComment());
                    response.setCreatedAt(review.getCreatedAt());

                    return response;

                })
                .toList();
    }
    @Override
    public ReviewResponseDto updateReview(
            String email,
            Long reviewId,
            ReviewRequestDto request) {

        // Find User
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        // Find Review
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Review not found"));

        // Check Ownership
        if (!review.getUser().getId().equals(user.getId())) {
            throw new RuntimeException(
                    "You can update only your own review");
        }

        // Update Review
        review.setRating(request.getRating());
        review.setComment(request.getComment());

        // Save
        Review updatedReview = reviewRepository.save(review);

        // Response
        ReviewResponseDto response = new ReviewResponseDto();

        response.setReviewId(updatedReview.getId());
        response.setProductId(updatedReview.getProduct().getId());
        response.setProductName(updatedReview.getProduct().getName());
        response.setUserName(updatedReview.getUser().getFullName());
        response.setRating(updatedReview.getRating());
        response.setComment(updatedReview.getComment());
        response.setCreatedAt(updatedReview.getCreatedAt());

        return response;
    }
    @Override
    public void deleteReview(
            String email,
            Long reviewId) {

        // Find User
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        // Find Review
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Review not found"));

        // Check Ownership
        if (!review.getUser().getId().equals(user.getId())) {
            throw new RuntimeException(
                    "You can delete only your own review");
        }

        // Delete Review
        reviewRepository.delete(review);
    }
    @Override
    public Double getAverageRating(Long productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found"));

        Double averageRating =
                reviewRepository.getAverageRating(product);

        return averageRating != null ? averageRating : 0.0;
    }
    @Override
    public Long getReviewCount(Long productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found"));

        return reviewRepository.getReviewCount(product);
    }
}