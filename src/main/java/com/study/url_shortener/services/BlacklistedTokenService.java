package com.study.url_shortener.services;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.study.url_shortener.entities.BlacklistedToken;
import com.study.url_shortener.repositories.BlacklistedTokenRepository;

@Service
public class BlacklistedTokenService {
    @Autowired
    private BlacklistedTokenRepository blacklistedTokenRepository;

    public void add(String token, LocalDateTime expiryDate) {
        BlacklistedToken blacklistedToken = new BlacklistedToken();
        blacklistedToken.setToken(token);
        blacklistedToken.setExpiryDate(expiryDate);

        blacklistedTokenRepository.save(blacklistedToken);
    }

    public boolean isTokenBlacklisted(String token) {
        return blacklistedTokenRepository.existsById(token);
    }

    @Scheduled(fixedRate = 3600000)
    public void deleteExpiredTokens() {
        LocalDateTime now = LocalDateTime.now();
        blacklistedTokenRepository.deleteAll(
                blacklistedTokenRepository.findByExpiryDateBefore(now));
    }
}
