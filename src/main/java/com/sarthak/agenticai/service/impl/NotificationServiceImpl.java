package com.sarthak.agenticai.service.impl;

import com.sarthak.agenticai.entity.Order;
import com.sarthak.agenticai.entity.User;
import com.sarthak.agenticai.service.NotificationService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final JavaMailSender mailSender;

    public NotificationServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
    @Override
    public void sendWelcomeEmail(User user) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(user.getEmail());

        message.setSubject("Welcome to Agentic AI Academic Assistant 🎉");

        message.setText(
                "Hello " + user.getFullName() + ",\n\n" +

                        "Welcome to Agentic AI Academic Assistant.\n\n" +

                        "Your account has been created successfully.\n\n" +

                        "We're excited to have you with us!\n\n" +

                        "Thank You,\n" +
                        "Agentic AI Team"
        );

        mailSender.send(message);
    }
    @Override
    public void sendOrderConfirmationEmail(Order order) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(order.getUser().getEmail());

        message.setSubject("Your Order has been Placed Successfully");

        message.setText(

                "Hello " + order.getUser().getFullName() + ",\n\n" +

                        "Thank you for shopping with us.\n\n" +

                        "Your order has been placed successfully.\n\n" +

                        "Order Details\n" +

                        "----------------------------------\n" +

                        "Order ID : " + order.getId() + "\n" +

                        "Order Date : " + order.getOrderDate() + "\n" +

                        "Order Status : " + order.getStatus() + "\n" +

                        "Total Amount : ₹" + order.getTotalAmount() + "\n\n" +

                        "We will notify you once your order is shipped.\n\n" +

                        "Thank You,\n" +

                        "Agentic AI Team"
        );

        mailSender.send(message);
    }
    @Override
    public void sendOrderCancelledEmail(Order order) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(order.getUser().getEmail());

        message.setSubject("Your Order has been Cancelled");

        message.setText(

                "Hello " + order.getUser().getFullName() + ",\n\n" +

                        "Your order has been cancelled successfully.\n\n" +

                        "Order Details\n" +

                        "----------------------------------\n" +

                        "Order ID : " + order.getId() + "\n" +

                        "Order Date : " + order.getOrderDate() + "\n" +

                        "Order Status : " + order.getStatus() + "\n" +

                        "Refund (if applicable) will be processed shortly.\n\n" +

                        "Thank You,\n" +

                        "Agentic AI Team"
        );

        mailSender.send(message);
    }
    @Override
    public void sendPaymentSuccessEmail(Order order) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(order.getUser().getEmail());

        message.setSubject("Payment Successful");

        message.setText(

                "Hello " + order.getUser().getFullName() + ",\n\n" +

                        "Your payment has been received successfully.\n\n" +

                        "Payment Details\n" +

                        "----------------------------------\n" +

                        "Order ID : " + order.getId() + "\n" +

                        "Amount Paid : ₹" + order.getTotalAmount() + "\n" +

                        "Payment Status : SUCCESS\n" +

                        "Order Status : " + order.getStatus() + "\n\n" +

                        "Thank you for shopping with us.\n\n" +

                        "Regards,\n" +

                        "Agentic AI Team"
        );

        mailSender.send(message);
    }

}
