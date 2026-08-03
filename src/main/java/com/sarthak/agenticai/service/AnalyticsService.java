package com.sarthak.agenticai.service;

import com.sarthak.agenticai.dto.*;

import java.util.List;

public interface AnalyticsService {

    RevenueAnalyticsDto getRevenueAnalytics();
    List<TopSellingProductDto> getTopSellingProducts();
    List<MonthlySalesDto> getMonthlySalesAnalytics();
    List<CategoryRevenueDto> getCategoryRevenue();
    InventoryAnalyticsDto getInventoryAnalytics();

}