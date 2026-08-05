package com.sarthak.agenticai.ai.service.impl;

import com.sarthak.agenticai.ai.service.AIService;
import com.sarthak.agenticai.repository.ProductRepository;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class AIServiceImpl implements AIService {

    private final ChatClient chatClient;
    private final ProductRepository productRepository;

    public AIServiceImpl(
            ChatClient.Builder builder,
            ProductRepository productRepository) {

        this.chatClient = builder.build();
        this.productRepository = productRepository;
    }
    @Override
    public String chat(String message) {

        String prompt = message.toLowerCase();

        // Low stock products
        if (prompt.contains("low stock")) {
            return productRepository.getLowStockProducts().toString();
        }

        // Out of stock products
        if (prompt.contains("out of stock")) {
            return productRepository.getOutOfStockProducts().toString();
        }

        // Inventory value
        if (prompt.contains("inventory value")) {
            return "Total Inventory Value = ₹"
                    + productRepository.getInventoryValue();
        }

        // Product summary
        if (prompt.contains("all products")) {
            return productRepository.getProductSummaryDto().toString();
        }

        // Otherwise ask OpenAI
        return chatClient
                .prompt(message)
                .call()
                .content();
    }
}