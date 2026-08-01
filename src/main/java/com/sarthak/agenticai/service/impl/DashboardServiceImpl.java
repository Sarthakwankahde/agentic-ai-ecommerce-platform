package com.sarthak.agenticai.service.impl;

import com.sarthak.agenticai.dto.DashboardResponseDto;
import com.sarthak.agenticai.entity.OrderStatus;
import com.sarthak.agenticai.repository.OrderRepository;
import com.sarthak.agenticai.repository.ProductRepository;
import com.sarthak.agenticai.repository.UserRepository;
import com.sarthak.agenticai.service.DashboardService;
import org.springframework.stereotype.Service;

@Service
public class DashboardServiceImpl implements DashboardService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public DashboardServiceImpl(
            UserRepository userRepository,
            ProductRepository productRepository,
            OrderRepository orderRepository) {

        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public DashboardResponseDto getDashboard() {

        DashboardResponseDto response = new DashboardResponseDto();

        response.setTotalUsers(userRepository.count());

        response.setTotalProducts(productRepository.count());

        response.setTotalOrders(orderRepository.count());

        response.setTotalRevenue(orderRepository.getTotalRevenue());

        response.setPendingOrders(
                orderRepository.countByStatus(OrderStatus.PENDING));

        response.setCompletedOrders(
                orderRepository.countByStatus(OrderStatus.DELIVERED));

        response.setCancelledOrders(
                orderRepository.countByStatus(OrderStatus.CANCELLED));

        return response;
    }
}