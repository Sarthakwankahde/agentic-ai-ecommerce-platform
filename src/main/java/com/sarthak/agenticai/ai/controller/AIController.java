package com.sarthak.agenticai.ai.controller;

import com.sarthak.agenticai.ai.service.AIService;
import com.sarthak.agenticai.ai.service.AIShoppingService;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
@RestController
@RequestMapping("/api/v1/ai")
public class AIController {

    private final AIShoppingService aiShoppingService;

    public AIController(AIShoppingService aiShoppingService) {
        this.aiShoppingService = aiShoppingService;
    }
    @GetMapping
    public String ask(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam String message) {

        return aiShoppingService.ask(
                userDetails.getUsername(),
                message
        );
    }
}