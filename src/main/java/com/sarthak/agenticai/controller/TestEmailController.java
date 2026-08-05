package com.sarthak.agenticai.controller;

import com.sarthak.agenticai.service.EmailService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestEmailController {

    private final EmailService emailService;

    public TestEmailController(
            EmailService emailService) {

        this.emailService = emailService;
    }

    @GetMapping("/api/test/email")
    public String sendEmail() {

        emailService.sendEmail(

                "sarthakwankhade2003.sw@gmail.com",

                "Spring Boot Email Test",

                "Congratulations! Email Notification Module is working successfully."

        );

        return "Email Sent Successfully!";
    }
}