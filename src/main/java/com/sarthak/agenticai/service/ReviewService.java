package com.sarthak.agenticai.service;

import com.sarthak.agenticai.dto.ReviewRequestDto;
import com.sarthak.agenticai.dto.ReviewResponseDto;

import java.util.List;

public interface ReviewService {

    // Add Review
    ReviewResponseDto addReview(
            String email,
            ReviewRequestDto request
    );

    // Get All Reviews of a Product
    List<ReviewResponseDto> getReviewsByProduct(
            Long productId
    );

    // Update Review
    ReviewResponseDto updateReview(
            String email,
            Long reviewId,
            ReviewRequestDto request
    );

    // Delete Review
    void deleteReview(
            String email,
            Long reviewId
    );

    // Get Average Rating
    Double getAverageRating(
            Long productId
    );

    // Get Total Review Count
    Long getReviewCount(
            Long productId
    );
}