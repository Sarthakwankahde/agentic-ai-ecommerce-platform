package com.sarthak.agenticai.controller;

import com.sarthak.agenticai.dto.CategoryRevenueDto;
import com.sarthak.agenticai.dto.MonthlySalesDto;
import com.sarthak.agenticai.dto.RevenueAnalyticsDto;
import com.sarthak.agenticai.service.AnalyticsService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.sarthak.agenticai.dto.TopSellingProductDto;
import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/revenue")
    @PreAuthorize("hasRole('ADMIN')")
    public RevenueAnalyticsDto getRevenueAnalytics() {

        return analyticsService.getRevenueAnalytics();

    }
    @GetMapping("/top-products")
    @PreAuthorize("hasRole('ADMIN')")
    public List<TopSellingProductDto> getTopSellingProducts() {

        return analyticsService.getTopSellingProducts();

    }
    @GetMapping("/monthly-sales")
    @PreAuthorize("hasRole('ADMIN')")
    public List<MonthlySalesDto> getMonthlySalesAnalytics() {

        return analyticsService.getMonthlySalesAnalytics();

    }
    @GetMapping("/category-revenue")
    @PreAuthorize("hasRole('ADMIN')")
    public List<CategoryRevenueDto> getCategoryRevenue() {

        return analyticsService.getCategoryRevenue();

    }
}