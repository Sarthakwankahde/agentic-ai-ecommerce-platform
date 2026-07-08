package com.sarthak.agenticai.controller;

import com.sarthak.agenticai.dto.ReviewRequestDto;
import com.sarthak.agenticai.dto.ReviewResponseDto;
import com.sarthak.agenticai.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    public ReviewResponseDto addReview(
            @RequestParam String email,
            @RequestBody @Valid ReviewRequestDto request) {

        return reviewService.addReview(email, request);
    }

    @GetMapping("/product/{productId}")
    public List<ReviewResponseDto> getReviewsByProduct(
            @PathVariable Long productId) {

        return reviewService.getReviewsByProduct(productId);
    }

    @PutMapping("/{reviewId}")
    public ReviewResponseDto updateReview(
            @RequestParam String email,
            @PathVariable Long reviewId,
            @RequestBody @Valid ReviewRequestDto request) {

        return reviewService.updateReview(
                email,
                reviewId,
                request);
    }

    @DeleteMapping("/{reviewId}")
    public void deleteReview(
            @RequestParam String email,
            @PathVariable Long reviewId) {

        reviewService.deleteReview(email, reviewId);
    }

    @GetMapping("/product/{productId}/average-rating")
    public Double getAverageRating(
            @PathVariable Long productId) {

        return reviewService.getAverageRating(productId);
    }

    @GetMapping("/product/{productId}/count")
    public Long getReviewCount(
            @PathVariable Long productId) {

        return reviewService.getReviewCount(productId);
    }
}