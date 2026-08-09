package com.sarthak.agenticai.controller;

import com.sarthak.agenticai.dto.ForgotPasswordRequestDto;
import com.sarthak.agenticai.dto.LoginRequestDto;
import com.sarthak.agenticai.dto.LoginResponseDto;
import com.sarthak.agenticai.dto.RefreshTokenRequestDto;
import com.sarthak.agenticai.dto.ResetPasswordRequestDto;
import com.sarthak.agenticai.exception.TooManyRequestsException;
import com.sarthak.agenticai.security.CustomUserDetails;
import com.sarthak.agenticai.service.AuthService;

import io.github.bucket4j.Bucket;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService service;

    private final Bucket forgotPasswordBucket;
    private final Bucket resetPasswordBucket;

    public AuthController(
            AuthService service,

            @Qualifier("forgotPasswordBucket")
            Bucket forgotPasswordBucket,

            @Qualifier("resetPasswordBucket")
            Bucket resetPasswordBucket) {

        this.service = service;
        this.forgotPasswordBucket = forgotPasswordBucket;
        this.resetPasswordBucket = resetPasswordBucket;
    }

    // =========================
    // LOGIN
    // =========================

    @PostMapping("/login")
    public LoginResponseDto login(
            @Valid @RequestBody LoginRequestDto request) {

        return service.login(request);
    }

    // =========================
    // REFRESH TOKEN
    // =========================

    @PostMapping("/refresh")
    public LoginResponseDto refreshToken(
            @RequestBody RefreshTokenRequestDto request) {

        return service.refreshToken(request);
    }

    // =========================
    // LOGOUT
    // =========================

    @PostMapping("/logout")
    public String logout(
            Authentication authentication) {

        CustomUserDetails user =
                (CustomUserDetails) authentication.getPrincipal();

        service.logout(user.getUsername());

        return "Logout Successful";
    }

    // =========================
    // FORGOT PASSWORD
    // =========================

    @PostMapping("/forgot-password")
    public String forgotPassword(
            @Valid @RequestBody ForgotPasswordRequestDto request) {

        /*
         * Rate limiting
         */
        if (!forgotPasswordBucket.tryConsume(1)) {

            throw new TooManyRequestsException(
                    "Too many password reset requests. Please try again later."
            );
        }

        return service.forgotPassword(request);
    }

    // =========================
    // RESET PASSWORD
    // =========================

    @PostMapping("/reset-password")
    public String resetPassword(
            @Valid @RequestBody ResetPasswordRequestDto request) {

        /*
         * Rate limiting
         */
        if (!resetPasswordBucket.tryConsume(1)) {

            throw new TooManyRequestsException(
                    "Too many password reset attempts. Please try again later."
            );
        }

        return service.resetPassword(request);
    }
}