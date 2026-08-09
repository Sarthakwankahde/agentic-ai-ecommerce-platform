package com.sarthak.agenticai.ai.service.impl;

import com.sarthak.agenticai.ai.service.AIShoppingService;
import com.sarthak.agenticai.ai.tool.AnalyticsTool;
import com.sarthak.agenticai.ai.tool.CartTool;
import com.sarthak.agenticai.ai.tool.OrderTool;
import com.sarthak.agenticai.ai.tool.PaymentTool;
import com.sarthak.agenticai.ai.tool.ProductTool;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
public class AIShoppingServiceImpl implements AIShoppingService {

    private final ChatClient chatClient;

    private final ProductTool productTool;
    private final OrderTool orderTool;
    private final CartTool cartTool;
    private final PaymentTool paymentTool;
    private final AnalyticsTool analyticsTool;

    public AIShoppingServiceImpl(
            ChatClient.Builder builder,
            ProductTool productTool,
            OrderTool orderTool,
            CartTool cartTool,
            PaymentTool paymentTool,
            AnalyticsTool analyticsTool,
            ChatMemory chatMemory
    ) {

        this.productTool = productTool;
        this.orderTool = orderTool;
        this.cartTool = cartTool;
        this.paymentTool = paymentTool;
        this.analyticsTool = analyticsTool;

        this.chatClient = builder
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(chatMemory)
                                .build()
                )
                .build();
    }
    @Override
    public String ask(String email, String message) {

        String systemPrompt = """
            You are an intelligent AI Shopping Assistant
            for an e-commerce platform.

            Your responsibilities include:

            1. Helping customers find products.
            2. Searching and recommending products.
            3. Managing shopping carts.
            4. Checking customer orders.
            5. Cancelling customer orders.
            6. Checking payment information.
            7. Providing business analytics when requested.

            IMPORTANT RULES:

            - Use tools whenever the user asks for information
              that can be obtained from the application.
            - Never invent product information.
            - Never invent order information.
            - Never invent payment information.
            - Never invent prices or stock information.
            - Give concise and helpful responses.
            - For shopping operations, use the appropriate tool.
            """;

        return chatClient
                .prompt()
                .system(systemPrompt)
                .user(message)

                .tools(
                        productTool,
                        orderTool,
                        cartTool,
                        paymentTool,
                        analyticsTool
                )

                .toolContext(
                        Map.of("email", email)
                )

                .advisors(advisor -> advisor.param(
                        ChatMemory.CONVERSATION_ID,
                        email
                ))

                .call()
                .content();
    }
}