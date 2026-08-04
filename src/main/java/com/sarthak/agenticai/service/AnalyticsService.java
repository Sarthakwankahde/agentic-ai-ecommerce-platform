package com.sarthak.agenticai.service;

import com.sarthak.agenticai.dto.*;

import java.util.List;

public interface AnalyticsService {

    RevenueAnalyticsDto getRevenueAnalytics();
    List<TopSellingProductDto> getTopSellingProducts();
    List<MonthlySalesDto> getMonthlySalesAnalytics();
    List<CategoryRevenueDto> getCategoryRevenue();
    InventoryAnalyticsDto getInventoryAnalytics();
    List<BestCustomerDto> getBestCustomers();
    List<RecentOrderDto> getRecentOrders();
    List<SalesTrendDto> getSalesTrend();
    List<OrderStatusAnalyticsDto> getOrderStatusAnalytics();
    List<CustomerGrowthDto> getCustomerGrowthAnalytics();

}