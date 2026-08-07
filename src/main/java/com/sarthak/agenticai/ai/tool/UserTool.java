package com.sarthak.agenticai.ai.tool;

import com.sarthak.agenticai.dto.ChangePasswordRequestDto;
import com.sarthak.agenticai.dto.ProfileResponseDto;
import com.sarthak.agenticai.dto.UpdateProfileRequestDto;
import com.sarthak.agenticai.service.impl.UserServiceImpl;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

@Component
public class UserTool {

    private final UserServiceImpl userService;

    public UserTool(UserServiceImpl userService) {
        this.userService = userService;
    }

    @Tool(description = "Returns the logged-in user's profile")
    public String getMyProfile(String email) {

        ProfileResponseDto profile =
                userService.getProfile(email);

        return """
                User Profile

                Name : %s
                Email : %s
                Role : %s
                """
                .formatted(
                        profile.getFullName(),
                        profile.getEmail(),
                        profile.getRole()
                );
    }

    @Tool(description = "Updates the user's full name")
    public String updateProfile(
            String email,
            String fullName) {

        UpdateProfileRequestDto request =
                new UpdateProfileRequestDto();

        request.setFullName(fullName);

        ProfileResponseDto profile =
                userService.updateProfile(email, request);

        return """
                Profile Updated Successfully

                Name : %s
                Email : %s
                Role : %s
                """
                .formatted(
                        profile.getFullName(),
                        profile.getEmail(),
                        profile.getRole()
                );
    }

    @Tool(description = "Changes the user's password")
    public String changePassword(
            String email,
            String currentPassword,
            String newPassword,
            String confirmPassword) {

        ChangePasswordRequestDto request =
                new ChangePasswordRequestDto();

        request.setCurrentPassword(currentPassword);
        request.setNewPassword(newPassword);
        request.setConfirmPassword(confirmPassword);

        return userService.changePassword(email, request);
    }
}