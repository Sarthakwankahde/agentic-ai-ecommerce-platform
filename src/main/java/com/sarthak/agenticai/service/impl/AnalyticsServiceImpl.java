package com.sarthak.agenticai.service.impl;

import com.sarthak.agenticai.dto.*;
import com.sarthak.agenticai.repository.OrderRepository;
import com.sarthak.agenticai.repository.UserRepository;
import com.sarthak.agenticai.service.AnalyticsService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.sarthak.agenticai.repository.OrderItemRepository;

import java.util.List;
import java.util.ArrayList;
import java.util.List;

import com.sarthak.agenticai.repository.ProductRepository;

@Service
public class AnalyticsServiceImpl implements AnalyticsService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    public AnalyticsServiceImpl(OrderRepository orderRepository,
                                OrderItemRepository orderItemRepository,
                                ProductRepository productRepository,
                                UserRepository userRepository) {

        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }
    @Override
    public RevenueAnalyticsDto getRevenueAnalytics() {

        RevenueAnalyticsDto response = new RevenueAnalyticsDto();

        // Total Revenue
        response.setTotalRevenue(orderRepository.getTotalRevenue());

        // ===========================
        // Today's Revenue
        // ===========================

        LocalDate today = LocalDate.now();

        LocalDateTime todayStart = today.atStartOfDay();

        LocalDateTime todayEnd = today.plusDays(1).atStartOfDay().minusSeconds(1);

        response.setTodayRevenue(
                orderRepository.getRevenueBetweenDates(todayStart, todayEnd)
        );

        // ===========================
        // Monthly Revenue
        // ===========================

        LocalDate firstDayOfMonth = today.withDayOfMonth(1);

        LocalDateTime monthStart = firstDayOfMonth.atStartOfDay();

        LocalDateTime monthEnd = firstDayOfMonth
                .plusMonths(1)
                .atStartOfDay()
                .minusSeconds(1);

        response.setMonthlyRevenue(
                orderRepository.getRevenueBetweenDates(monthStart, monthEnd)
        );

        // ===========================
        // Yearly Revenue
        // ===========================

        LocalDate firstDayOfYear = today.withDayOfYear(1);

        LocalDateTime yearStart = firstDayOfYear.atStartOfDay();

        LocalDateTime yearEnd = firstDayOfYear
                .plusYears(1)
                .atStartOfDay()
                .minusSeconds(1);

        response.setYearlyRevenue(
                orderRepository.getRevenueBetweenDates(yearStart, yearEnd)
        );

        return response;
    }
    @Override
    public List<TopSellingProductDto> getTopSellingProducts() {

        return orderItemRepository.getTopSellingProducts();

    }
    @Override
    public List<MonthlySalesDto> getMonthlySalesAnalytics() {

        List<Object[]> result = orderRepository.getMonthlySalesAnalytics();

        List<MonthlySalesDto> response = new ArrayList<>();

        for (Object[] row : result) {

            response.add(
                    new MonthlySalesDto(

                            row[0].toString().trim(),

                            ((Number) row[1]).doubleValue(),

                            ((Number) row[2]).longValue()
                    )
            );
        }

        return response;
    }
    @Override
    public List<CategoryRevenueDto> getCategoryRevenue() {

        return orderItemRepository.getCategoryRevenue();

    }
    @Override
    public InventoryAnalyticsDto getInventoryAnalytics() {

        InventoryAnalyticsDto response = new InventoryAnalyticsDto();

        response.setInventoryValue(
                productRepository.getInventoryValue()
        );

        response.setLowStockProducts(
                (long) productRepository.getLowStockProducts().size()
        );

        response.setOutOfStockProducts(
                (long) productRepository.getOutOfStockProducts().size()
        );

        return response;
    }
    @Override
    public List<BestCustomerDto> getBestCustomers() {

        return orderRepository.getBestCustomers();

    }
    @Override
    public List<RecentOrderDto> getRecentOrders() {

        List<RecentOrderDto> orders = orderRepository.getRecentOrders();

        if (orders.size() > 10) {
            return orders.subList(0, 10);
        }

        return orders;
    }
    @Override
    public List<SalesTrendDto> getSalesTrend() {

        return orderRepository.getSalesTrend();

    }
    @Override
    public List<OrderStatusAnalyticsDto> getOrderStatusAnalytics() {

        return orderRepository.getOrderStatusAnalytics();

    }
    @Override
    public List<CustomerGrowthDto> getCustomerGrowthAnalytics() {

        return userRepository.getCustomerGrowthAnalytics();

    }
}