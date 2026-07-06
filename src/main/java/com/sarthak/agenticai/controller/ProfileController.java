package com.sarthak.agenticai.controller;

import com.sarthak.agenticai.dto.ChangePasswordRequestDto;
import com.sarthak.agenticai.dto.ProfileResponseDto;
import com.sarthak.agenticai.dto.UpdateProfileRequestDto;
import com.sarthak.agenticai.security.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.sarthak.agenticai.service.impl.UserServiceImpl;

@RestController
@RequestMapping("/api/v1")
public class ProfileController {
    private final UserServiceImpl userService;

    public ProfileController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public ProfileResponseDto profile(Authentication authentication) {

        CustomUserDetails user =
                (CustomUserDetails) authentication.getPrincipal();

        return new ProfileResponseDto(
                user.getId(),
                user.getFullName(),
                user.getUsername(),
                user.getRole().name()
        );
    }
    @PutMapping("/profile")
    public ProfileResponseDto updateProfile(
            Authentication authentication,
            @RequestBody UpdateProfileRequestDto request) {

        CustomUserDetails user =
                (CustomUserDetails) authentication.getPrincipal();

        return userService.updateProfile(
                user.getUsername(),
                request
        );
    }
    @PutMapping("/profile/change-password")
    public String changePassword(
            Authentication authentication,
            @RequestBody ChangePasswordRequestDto request) {

        CustomUserDetails user =
                (CustomUserDetails) authentication.getPrincipal();

        return userService.changePassword(
                user.getUsername(),
                request
        );
    }
}