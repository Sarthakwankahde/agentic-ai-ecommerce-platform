package com.sarthak.agenticai.service.impl;

import com.sarthak.agenticai.dto.CategoryRevenueDto;
import com.sarthak.agenticai.dto.RevenueAnalyticsDto;
import com.sarthak.agenticai.repository.OrderRepository;
import com.sarthak.agenticai.service.AnalyticsService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import com.sarthak.agenticai.dto.TopSellingProductDto;
import com.sarthak.agenticai.repository.OrderItemRepository;

import java.util.List;
import com.sarthak.agenticai.dto.MonthlySalesDto;
import java.util.ArrayList;
import java.util.List;

@Service
public class AnalyticsServiceImpl implements AnalyticsService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    public AnalyticsServiceImpl(
            OrderRepository orderRepository,
            OrderItemRepository orderItemRepository) {

        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
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
}