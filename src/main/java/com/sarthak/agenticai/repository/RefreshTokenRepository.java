package com.sarthak.agenticai.repository;

import com.sarthak.agenticai.entity.RefreshToken;
import com.sarthak.agenticai.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository
        extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    Optional<RefreshToken> findByUser(User user);

    void deleteByUser(User user);
}