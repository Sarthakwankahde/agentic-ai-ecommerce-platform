package com.sarthak.agenticai.service;

import com.sarthak.agenticai.dto.LoginRequestDto;
import com.sarthak.agenticai.dto.LoginResponseDto;
import com.sarthak.agenticai.dto.RefreshTokenRequestDto;

public interface AuthService {

    LoginResponseDto login(LoginRequestDto request);
    LoginResponseDto refreshToken(RefreshTokenRequestDto request);
    void logout(String email);

}