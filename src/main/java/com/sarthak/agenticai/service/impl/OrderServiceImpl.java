package com.sarthak.agenticai.service.impl;

import com.sarthak.agenticai.dto.OrderItemResponseDto;
import com.sarthak.agenticai.dto.OrderResponseDto;
import com.sarthak.agenticai.exception.ResourceNotFoundException;
import com.sarthak.agenticai.repository.*;
import com.sarthak.agenticai.service.OrderService;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

import com.sarthak.agenticai.entity.Order;
import com.sarthak.agenticai.entity.OrderItem;
import com.sarthak.agenticai.entity.OrderStatus;
import com.sarthak.agenticai.entity.Cart;
import com.sarthak.agenticai.entity.CartItem;
import com.sarthak.agenticai.entity.Product;
import com.sarthak.agenticai.entity.User;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public OrderServiceImpl(
            OrderRepository orderRepository,
            OrderItemRepository orderItemRepository,
            CartRepository cartRepository,
            CartItemRepository cartItemRepository,
            ProductRepository productRepository,
            UserRepository userRepository) {

        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Transactional
@Override
public OrderResponseDto placeOrder(String email) {

    // Find User
    User user = userRepository.findByEmail(email)
            .orElseThrow(() ->
                    new ResourceNotFoundException("User not found"));

    // Find Cart
    Cart cart = cartRepository.findByUser(user)
            .orElseThrow(() ->
                    new ResourceNotFoundException("Cart not found"));

    // Check Empty Cart
    if (cart.getCartItems().isEmpty()) {
        throw new RuntimeException("Cart is empty");
    }

    // Create Order
    Order order = new Order();
    order.setUser(user);
    order.setOrderDate(LocalDateTime.now());
    order.setStatus(OrderStatus.PENDING);

    double totalAmount = 0.0;

    List<OrderItemResponseDto> itemResponses = new ArrayList<>();

    // Convert CartItems -> OrderItems
    for (CartItem cartItem : cart.getCartItems()) {

        Product product = cartItem.getProduct();

        // Check Stock
        if (product.getQuantity() < cartItem.getQuantity()) {
            throw new RuntimeException(
                    product.getName() + " is out of stock");
        }

        // Create Order Item
        OrderItem orderItem = new OrderItem();

        orderItem.setOrder(order);
        orderItem.setProduct(product);
        orderItem.setQuantity(cartItem.getQuantity());
        orderItem.setPrice(product.getPrice());

        order.getOrderItems().add(orderItem);

        // Reduce Stock
        product.setQuantity(
                product.getQuantity() - cartItem.getQuantity());

        productRepository.save(product);

        // Calculate Total
        totalAmount +=
                product.getPrice() * cartItem.getQuantity();

        // Response Item
        itemResponses.add(

                new OrderItemResponseDto(

                        product.getId(),

                        product.getName(),

                        cartItem.getQuantity(),

                        product.getPrice()
                )
        );
    }

    order.setTotalAmount(totalAmount);

    // Save Order
    Order savedOrder = orderRepository.save(order);

    // Clear Cart
    cart.getCartItems().clear();

    cartRepository.save(cart);

    // Response
    OrderResponseDto response = new OrderResponseDto();

    response.setOrderId(savedOrder.getId());
    response.setOrderDate(savedOrder.getOrderDate());
    response.setStatus(savedOrder.getStatus());
    response.setTotalAmount(savedOrder.getTotalAmount());
    response.setItems(itemResponses);

    return response;
}
@Override
public List<OrderResponseDto> getMyOrders(String email) {

    User user = userRepository.findByEmail(email)
            .orElseThrow(() ->
                    new ResourceNotFoundException("User not found"));

    return orderRepository
            .findByUserOrderByOrderDateDesc(user)
            .stream()
            .map(order -> {

                OrderResponseDto response = new OrderResponseDto();

                response.setOrderId(order.getId());
                response.setOrderDate(order.getOrderDate());
                response.setStatus(order.getStatus());
                response.setTotalAmount(order.getTotalAmount());

                List<OrderItemResponseDto> items =
                        order.getOrderItems()
                                .stream()
                                .map(orderItem ->

                                        new OrderItemResponseDto(

                                                orderItem.getProduct().getId(),

                                                orderItem.getProduct().getName(),

                                                orderItem.getQuantity(),

                                                orderItem.getPrice()

                                        )
                                )
                                .toList();

                response.setItems(items);

                return response;

            })
            .toList();
}
@Override
public OrderResponseDto getOrderById(
        Long orderId,
        String email) {

    User user = userRepository.findByEmail(email)
            .orElseThrow(() ->
                    new ResourceNotFoundException("User not found"));

    Order order = orderRepository.findByIdAndUser(orderId, user)
            .orElseThrow(() ->
                    new ResourceNotFoundException("Order not found"));

    OrderResponseDto response = new OrderResponseDto();

    response.setOrderId(order.getId());
    response.setOrderDate(order.getOrderDate());
    response.setStatus(order.getStatus());
    response.setTotalAmount(order.getTotalAmount());

    List<OrderItemResponseDto> items =
            order.getOrderItems()
                    .stream()
                    .map(orderItem ->

                            new OrderItemResponseDto(

                                    orderItem.getProduct().getId(),

                                    orderItem.getProduct().getName(),

                                    orderItem.getQuantity(),

                                    orderItem.getPrice()

                            )
                    )
                    .toList();

    response.setItems(items);

    return response;
}
@Transactional
@Override
public void cancelOrder(
        Long orderId,
        String email) {

    User user = userRepository.findByEmail(email)
            .orElseThrow(() ->
                    new ResourceNotFoundException("User not found"));

    Order order = orderRepository.findByIdAndUser(orderId, user)
            .orElseThrow(() ->
                    new ResourceNotFoundException("Order not found"));

    if (order.getStatus() == OrderStatus.CANCELLED) {
        throw new RuntimeException("Order is already cancelled");
    }

    if (order.getStatus() == OrderStatus.SHIPPED) {
        throw new RuntimeException("Shipped order cannot be cancelled");
    }

    if (order.getStatus() == OrderStatus.DELIVERED) {
        throw new RuntimeException("Delivered order cannot be cancelled");
    }

    // Restore Stock
    for (OrderItem orderItem : order.getOrderItems()) {

        Product product = orderItem.getProduct();

        product.setQuantity(
                product.getQuantity() + orderItem.getQuantity());

        productRepository.save(product);
    }

    order.setStatus(OrderStatus.CANCELLED);

    orderRepository.save(order);
      }
   }