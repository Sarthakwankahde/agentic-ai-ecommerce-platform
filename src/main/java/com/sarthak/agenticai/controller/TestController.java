package com.sarthak.agenticai.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/api/v1/user/dashboard")
    public String userDashboard() {
        return "Welcome User Dashboard";
    }

    @GetMapping("/api/v1/admin/dashboard")
    public String adminDashboard() {
        return "Welcome Admin Dashboard";
    }
}