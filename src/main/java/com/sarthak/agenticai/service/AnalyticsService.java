package com.sarthak.agenticai.service;

import com.sarthak.agenticai.dto.CategoryRevenueDto;
import com.sarthak.agenticai.dto.MonthlySalesDto;
import com.sarthak.agenticai.dto.RevenueAnalyticsDto;
import com.sarthak.agenticai.dto.TopSellingProductDto;

import java.util.List;

public interface AnalyticsService {

    RevenueAnalyticsDto getRevenueAnalytics();
    List<TopSellingProductDto> getTopSellingProducts();
    List<MonthlySalesDto> getMonthlySalesAnalytics();
    List<CategoryRevenueDto> getCategoryRevenue();

}