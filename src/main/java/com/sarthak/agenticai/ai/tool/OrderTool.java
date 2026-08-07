package com.sarthak.agenticai.ai.tool;

import com.sarthak.agenticai.dto.OrderResponseDto;
import com.sarthak.agenticai.service.OrderService;
import org.springframework.stereotype.Component;
import org.springframework.ai.tool.annotation.Tool;

import java.util.List;

@Component
public class OrderTool {

    private final OrderService orderService;

    public OrderTool(OrderService orderService) {
        this.orderService = orderService;
    }
    @Tool(description = "Returns all orders placed by the current customer")
    public String getMyOrders(String email) {

        return buildOrderResponse(
                orderService.getMyOrders(email)
        );
    }
    @Tool(description = "Returns complete details of an order using the order ID")
    public String getOrderById(String email,
                               Long orderId) {

        OrderResponseDto order =
                orderService.getOrderById(orderId, email);

        return buildOrderResponse(List.of(order));
    }
    @Tool(description = "Cancels an existing order using its order ID")
    public String cancelOrder(String email,
                              Long orderId) {

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