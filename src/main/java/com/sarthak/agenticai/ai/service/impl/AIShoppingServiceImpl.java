    package com.sarthak.agenticai.ai.service.impl;

    import com.sarthak.agenticai.ai.service.AIShoppingService;
    import com.sarthak.agenticai.dto.ProductResponseDto;
    import com.sarthak.agenticai.service.AnalyticsService;
    import com.sarthak.agenticai.service.OrderService;
    import com.sarthak.agenticai.service.PaymentService;
    import com.sarthak.agenticai.service.ProductService;
    import org.springframework.ai.chat.client.ChatClient;
    import org.springframework.stereotype.Service;

    @Service
    public class AIShoppingServiceImpl implements AIShoppingService {

        private final ChatClient chatClient;

        private final ProductService productService;

        private final OrderService orderService;

        private final AnalyticsService analyticsService;

        private final PaymentService paymentService;

        public AIShoppingServiceImpl(
                ChatClient.Builder builder,
                ProductService productService,
                OrderService orderService,
                AnalyticsService analyticsService,
                PaymentService paymentService
        ) {

            this.chatClient = builder.build();

            this.productService = productService;

            this.orderService = orderService;

            this.analyticsService = analyticsService;

            this.paymentService = paymentService;
        }
        @Override
        public String ask(String email, String message) {

            String lowerMessage = message.toLowerCase();

            // Product Questions
            if (lowerMessage.contains("product")
                        || lowerMessage.contains("laptop")
                        || lowerMessage.contains("phone")
                        || lowerMessage.contains("mobile")
                        || lowerMessage.contains("electronics")
                        || lowerMessage.contains("computer")
                        || lowerMessage.contains("headphone")
                        || lowerMessage.contains("watch")
                        || lowerMessage.contains("shoe")){
                    var products = productService.getAllProducts(
                            0,
                            10,
                            "id",
                            "asc"
                    );

                    StringBuilder prompt = new StringBuilder();

                    prompt.append("""
    You are an expert AI shopping assistant.
                            
    Answer ONLY using the products listed below.
                            
   If the requested product is not available, politely say so.
                            
    Recommend products based on price, stock, category and description.
    
    These are the products available:
    
    """);
                int index = 1;

                for (ProductResponseDto product : products.getContent()) {

                    prompt.append("""
Product %d
Name : %s
Category : %s
Price : ₹%s
Stock : %s units
Description : %s

"""
                            .formatted(
                                    index++,
                                    product.getName(),
                                    product.getCategoryName(),
                                    product.getPrice(),
                                    product.getQuantity(),
                                    product.getDescription()
                            ));
                }


                    prompt.append("""
    
    User Question:
    
    """);

                    prompt.append(message);

                    return chatClient
                            .prompt(prompt.toString())
                            .call()
                            .content();
                }



            // Order Questions

            if (lowerMessage.contains("order")) {

                var orders = orderService.getMyOrders(email);

                if (orders.isEmpty()) {
                    return "You don't have any orders yet.";
                }

                StringBuilder prompt = new StringBuilder();

                prompt.append("""
You are an AI Order Assistant.

Use ONLY the order information below.

Do not invent any information.

Customer Orders:

""");

                int index = 1;

                for (var order : orders) {

                    prompt.append("""
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

                prompt.append("""

Customer Question:

""");

                prompt.append(message);

                return chatClient
                        .prompt(prompt.toString())
                        .call()
                        .content();
            }

            // Revenue Questions
            if (lowerMessage.contains("revenue")
                    || lowerMessage.contains("sales")) {

                var revenue = analyticsService.getRevenueAnalytics();

                String prompt = """
            You are an AI Business Analyst.

            Revenue Details

            Total Revenue : ₹%s

            Today Revenue : ₹%s

            Monthly Revenue : ₹%s

            Yearly Revenue : ₹%s

            Explain this in simple English.
            """
                        .formatted(
                                revenue.getTotalRevenue(),
                                revenue.getTodayRevenue(),
                                revenue.getMonthlyRevenue(),
                                revenue.getYearlyRevenue()
                        );

                return chatClient
                        .prompt(prompt)
                        .call()
                        .content();
            }

            // Payment Questions
            if (lowerMessage.contains("payment")) {

                return """
            Payment features available:

            • Create Payment
            • Verify Payment
            • Razorpay Integration

            User-specific payment queries will be connected after authentication integration.
            """;
            }

            // Default → GPT
            return chatClient
                    .prompt(message)
                    .call()
                    .content();
        }
    }