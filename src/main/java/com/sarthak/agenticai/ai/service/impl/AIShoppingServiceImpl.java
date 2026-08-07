    package com.sarthak.agenticai.ai.service.impl;

    import com.sarthak.agenticai.ai.service.AIShoppingService;
    import com.sarthak.agenticai.dto.ProductResponseDto;
    import com.sarthak.agenticai.service.AnalyticsService;
    import com.sarthak.agenticai.service.OrderService;
    import com.sarthak.agenticai.service.PaymentService;
    import com.sarthak.agenticai.service.ProductService;
    import org.springframework.ai.chat.client.ChatClient;
    import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
    import org.springframework.ai.chat.memory.ChatMemory;
    import org.springframework.stereotype.Service;
    import com.sarthak.agenticai.ai.tool.ProductTool;
    import com.sarthak.agenticai.ai.tool.OrderTool;
    import com.sarthak.agenticai.ai.tool.CartTool;
    import com.sarthak.agenticai.ai.tool.PaymentTool;
    import com.sarthak.agenticai.ai.tool.AnalyticsTool;
    import reactor.core.scheduler.Schedulers;

    @Service
    public class AIShoppingServiceImpl implements AIShoppingService {

        private final ChatClient chatClient;
        private final ProductTool productTool;

        private final OrderTool orderTool;

        private final CartTool cartTool;

        private final PaymentTool paymentTool;

        private final AnalyticsTool analyticsTool;

        private final ChatMemory chatMemory;

        public AIShoppingServiceImpl(
                ChatClient.Builder builder,
                ProductTool productTool,
                ChatMemory chatMemory,
                OrderTool orderTool,
                CartTool cartTool,
                PaymentTool paymentTool,
                AnalyticsTool analyticsTool
        ) {

            /*this.chatClient = builder.build();*/
            this.productTool = productTool;
            this.chatMemory = chatMemory;
            this.chatClient = builder
                    .defaultAdvisors(
                            MessageChatMemoryAdvisor.builder(chatMemory)
                                    .conversationId("default")
                                    .order(20)
                                    .scheduler(Schedulers.boundedElastic())
                                    .build()
                    )
                    .build();
            this.orderTool = orderTool;
            this.cartTool = cartTool;
            this.paymentTool = paymentTool;
            this.analyticsTool = analyticsTool;
        }
        @Override
        public String ask(String email, String message) {

            String lowerMessage = message.toLowerCase();

            // -------------------------------
            // Product Questions
            // -------------------------------
            if (lowerMessage.contains("product")
                    || lowerMessage.contains("laptop")
                    || lowerMessage.contains("phone")
                    || lowerMessage.contains("mobile")
                    || lowerMessage.contains("electronics")
                    || lowerMessage.contains("computer")
                    || lowerMessage.contains("headphone")
                    || lowerMessage.contains("watch")
                    || lowerMessage.contains("shoe")) {

                String productInfo = productTool.getAllProducts();

                String prompt = """
                You are an expert AI Shopping Assistant.

                Answer ONLY using the product information below.

                If a product is unavailable, politely inform the user.

                Available Products:

                %s

                Customer Question:
                %s
                """.formatted(productInfo, message);

                return chatClient.prompt(prompt)
                        .call()
                        .content();
            }

            // -------------------------------
            // Order Questions
            // -------------------------------
            if (lowerMessage.contains("order")
                    || lowerMessage.contains("track")
                    || lowerMessage.contains("history")
                    || lowerMessage.contains("status")) {

                String orderInfo = orderTool.getMyOrders(email);

                String prompt = """
                You are an AI Order Assistant.

                Answer ONLY using the order information below.

                %s

                Customer Question:
                %s
                """.formatted(orderInfo, message);

                return chatClient.prompt(prompt)
                        .call()
                        .content();
            }

            // -------------------------------
            // Revenue / Analytics Questions
            // -------------------------------
            if (lowerMessage.contains("revenue")
                    || lowerMessage.contains("sales")
                    || lowerMessage.contains("analytics")) {

                String analytics = analyticsTool.getRevenueAnalytics();

                String prompt = """
                You are an AI Business Analyst.

                Explain the following business analytics in simple English.

                %s
                """.formatted(analytics);

                return chatClient.prompt(prompt)
                        .call()
                        .content();
            }

            // -------------------------------
            // Payment Questions
            // -------------------------------
            if (lowerMessage.contains("payment")) {

                String paymentInfo = paymentTool.getMyPayments(email);

                String prompt = """
                You are an AI Payment Assistant.

                Explain the payment information below.

                %s

                Customer Question:
                %s
                """.formatted(paymentInfo, message);

                return chatClient.prompt(prompt)
                        .call()
                        .content();
            }

            // -------------------------------
            // Default
            // -------------------------------
            return chatClient
                    .prompt(message)
                    .advisors(a -> a.param(
                            ChatMemory.CONVERSATION_ID,
                            email
                    ))
                    .call()
                    .content();
        }    }