package com.sarthak.agenticai.service.impl;

import com.sarthak.agenticai.entity.RefreshToken;
import com.sarthak.agenticai.entity.User;
import com.sarthak.agenticai.repository.RefreshTokenRepository;
import com.sarthak.agenticai.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

    @Value("${jwt.refresh.expiration}")
    private Long refreshTokenDuration;

    private final RefreshTokenRepository repository;

    public RefreshTokenServiceImpl(RefreshTokenRepository repository) {
        this.repository = repository;
    }
    @Override
    public RefreshToken createRefreshToken(User user) {

        repository.findByUser(user)
                .ifPresent(repository::delete);

        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setUser(user);

        refreshToken.setToken(UUID.randomUUID().toString());

        refreshToken.setExpiryDate(
                Instant.now().plusMillis(refreshTokenDuration)
        );

        return repository.save(refreshToken);
    }
    @Override
    public RefreshToken verifyRefreshToken(String token) {

        RefreshToken refreshToken = repository.findByToken(token)
                .orElseThrow(() ->
                        new RuntimeException("Refresh Token not found"));

        if (refreshToken.getExpiryDate().isBefore(Instant.now())) {

            repository.delete(refreshToken);

            throw new RuntimeException("Refresh Token Expired");
        }

        return refreshToken;
    }
    @Override
    @Transactional
    public void deleteByUser(User user) {

        repository.deleteByUser(user);

    }
}