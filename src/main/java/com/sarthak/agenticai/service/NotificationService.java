package com.sarthak.agenticai.service;

import com.sarthak.agenticai.entity.Order;
import com.sarthak.agenticai.entity.User;

public interface NotificationService {

    void sendWelcomeEmail(User user);

    void sendOrderConfirmationEmail(Order order);

    void sendOrderCancelledEmail(Order order);

    void sendPaymentSuccessEmail(Order order);
}