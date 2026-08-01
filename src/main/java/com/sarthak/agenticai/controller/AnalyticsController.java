package com.sarthak.agenticai.controller;

import com.sarthak.agenticai.dto.RevenueAnalyticsDto;
import com.sarthak.agenticai.service.AnalyticsService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
}