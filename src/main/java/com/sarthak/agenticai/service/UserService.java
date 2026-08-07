package com.sarthak.agenticai.service;

import com.sarthak.agenticai.dto.ChangePasswordRequestDto;
import com.sarthak.agenticai.dto.ProfileResponseDto;
import com.sarthak.agenticai.dto.UpdateProfileRequestDto;
import com.sarthak.agenticai.dto.UserRequestDto;

public interface UserService {

    String registerUser(UserRequestDto request);
    ProfileResponseDto getProfile(String email);

    ProfileResponseDto updateProfile(
            String email,
            UpdateProfileRequestDto request);

    String changePassword(
            String email,
            ChangePasswordRequestDto request);

}
