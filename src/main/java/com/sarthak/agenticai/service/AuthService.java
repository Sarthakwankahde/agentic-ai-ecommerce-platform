package com.sarthak.agenticai.service;

import com.sarthak.agenticai.dto.*;

public interface AuthService {

    LoginResponseDto login(LoginRequestDto request);
    LoginResponseDto refreshToken(RefreshTokenRequestDto request);
    void logout(String email);
    String forgotPassword(ForgotPasswordRequestDto request);

    String resetPassword(ResetPasswordRequestDto request);

}