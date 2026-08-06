package com.sarthak.agenticai.ai.tool;

import com.sarthak.agenticai.service.AnalyticsService;
import org.springframework.stereotype.Component;

@Component
public class AnalyticsTool {

    private final AnalyticsService analyticsService;

    public AnalyticsTool(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    public String getRevenueAnalytics() {

        var revenue =
                analyticsService.getRevenueAnalytics();

        return """
                Revenue Analytics

                Total Revenue : ₹%s
                Today Revenue : ₹%s
                Monthly Revenue : ₹%s
                Yearly Revenue : ₹%s
                """
                .formatted(
                        revenue.getTotalRevenue(),
                        revenue.getTodayRevenue(),
                        revenue.getMonthlyRevenue(),
                        revenue.getYearlyRevenue()
                );
    }
}