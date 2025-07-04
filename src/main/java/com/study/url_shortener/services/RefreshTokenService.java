package com.study.url_shortener.services;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.study.url_shortener.entities.RefreshToken;
import com.study.url_shortener.entities.User;
import com.study.url_shortener.repositories.RefreshTokenRepository;
import com.study.url_shortener.repositories.UserRepository;

@Service
public class RefreshTokenService {
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Value("${jwt.refresh.expiration}")
    private Long EXPIRATION_DAYS;

    public String create(String username) {
        User user = userRepository.findById(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        String token = UUID.randomUUID().toString();

        RefreshToken refreshToken = refreshTokenRepository.findByUser(user).orElse(null);

        if (refreshToken != null) {
            refreshTokenRepository.delete(refreshToken);
        }

        refreshToken = new RefreshToken();
        refreshToken.setToken(token);
        refreshToken.setUser(user);
        refreshToken.setExpiryDate(LocalDateTime.now().plusDays(EXPIRATION_DAYS));

        refreshTokenRepository.save(refreshToken);

        return token;
    }

    public Optional<RefreshToken> verify(String token) {
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByToken(token);

        if (refreshToken.isPresent() && !isTokenExpired(refreshToken.get())) {
            return refreshToken;
        }

        refreshToken.ifPresent(refreshTokenRepository::delete);
        return Optional.empty();
    }

    private boolean isTokenExpired(RefreshToken refreshToken) {
        return refreshToken.getExpiryDate().isBefore(LocalDateTime.now());
    }
}
