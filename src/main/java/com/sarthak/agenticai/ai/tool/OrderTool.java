package com.sarthak.agenticai.ai.tool;

import com.sarthak.agenticai.dto.OrderResponseDto;
import com.sarthak.agenticai.service.OrderService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderTool {

    private final OrderService orderService;

    public OrderTool(OrderService orderService) {
        this.orderService = orderService;
    }

    public String getMyOrders(String email) {

        return buildOrderResponse(
                orderService.getMyOrders(email)
        );
    }

    public String getOrderById(Long orderId, String email) {

        OrderResponseDto order =
                orderService.getOrderById(orderId, email);

        return buildOrderResponse(List.of(order));
    }

    public String cancelOrder(Long orderId, String email) {

        orderService.cancelOrder(orderId, email);

        return "Order cancelled successfully.";
    }

    private String buildOrderResponse(List<OrderResponseDto> orders) {

        if (orders.isEmpty()) {
            return "No orders found.";
        }

        StringBuilder response = new StringBuilder();

        int index = 1;

        for (OrderResponseDto order : orders) {

            response.append("""
Order %d

Order ID : %d
Status : %s
Amount : ₹%s
Order Date : %s

"""
                    .formatted(
                            index++,
                            order.getOrderId(),
                            order.getStatus(),
                            order.getTotalAmount(),
                            order.getOrderDate()
                    ));
        }

        return response.toString();
    }
}