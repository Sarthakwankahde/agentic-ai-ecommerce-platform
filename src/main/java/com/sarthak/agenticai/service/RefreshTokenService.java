package com.sarthak.agenticai.service;

import com.sarthak.agenticai.entity.RefreshToken;
import com.sarthak.agenticai.entity.User;

public interface RefreshTokenService {

    RefreshToken createRefreshToken(User user);

    RefreshToken verifyRefreshToken(String token);

    void deleteByUser(User user);
}