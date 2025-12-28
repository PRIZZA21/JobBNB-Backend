package com.JobBNB.dev.auth.service;

import com.JobBNB.dev.auth.model.RefreshToken;
import com.JobBNB.dev.auth.repository.RefreshTokenRepository;
import com.JobBNB.dev.common.exception.BusinessException;
import com.JobBNB.dev.user.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Value("${jwt.refresh-token-expiry}")
    private long refreshTokenDurationMs;

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenService(RefreshTokenRepository repo) {
        this.refreshTokenRepository = repo;
    }

    public RefreshToken createRefreshToken(User user) {
        RefreshToken token = new RefreshToken();
        token.setUser(user);
        token.setToken(UUID.randomUUID().toString());
        token.setExpiresAt(
                Instant.now().plusMillis(refreshTokenDurationMs)
        );

        return refreshTokenRepository.save(token);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiresAt().isBefore(Instant.now())) {
            refreshTokenRepository.delete(token);
            throw new BusinessException("Refresh token expired");
        }
        return token;
    }
}
