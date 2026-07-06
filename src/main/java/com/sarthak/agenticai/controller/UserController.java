package com.sarthak.agenticai.controller;

import com.sarthak.agenticai.dto.UserRequestDto;
import com.sarthak.agenticai.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public String register(@Valid @RequestBody UserRequestDto request) {

        return service.registerUser(request);

    }
}