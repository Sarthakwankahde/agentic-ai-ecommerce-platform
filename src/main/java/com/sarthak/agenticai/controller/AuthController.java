package com.sarthak.agenticai.controller;

import com.sarthak.agenticai.dto.LoginRequestDto;
import com.sarthak.agenticai.dto.LoginResponseDto;
import com.sarthak.agenticai.dto.RefreshTokenRequestDto;
import com.sarthak.agenticai.security.CustomUserDetails;
import com.sarthak.agenticai.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/login")
    public LoginResponseDto login(@Valid @RequestBody LoginRequestDto request) {

        return service.login(request);

    }
    @PostMapping("/refresh")
    public LoginResponseDto refreshToken(
            @RequestBody RefreshTokenRequestDto request) {

        return service.refreshToken(request);
    }
    @PostMapping("/logout")
    public String logout(Authentication authentication) {

        CustomUserDetails user =
                (CustomUserDetails) authentication.getPrincipal();

        service.logout(user.getUsername());

        return "Logout Successful";
    }
}