package com.study.url_shortener.services;

import java.time.ZoneId;
import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.study.url_shortener.entities.RefreshToken;
import com.study.url_shortener.entities.User;
import com.study.url_shortener.enums.RoleEnum;
import com.study.url_shortener.models.user.AuthRequest;
import com.study.url_shortener.models.user.AuthResponse;
import com.study.url_shortener.repositories.UserRepository;
import com.study.url_shortener.utils.JwtUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final BlacklistedTokenService blacklistedTokenService;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final RefreshTokenService refreshTokenService;

    private final JwtUtil jwtUtil;

    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        
        RoleEnum roleEnum = user.getRole().getName();

        return org.springframework.security.core.userdetails.User
            .withUsername(user.getUsername())
            .password(user.getPassword())
            .authorities(roleEnum.getAuthority())
            .build();
    }

    public void register(AuthRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);
    }

    public AuthResponse refreshAccessToken(String refreshToken) {
        RefreshToken token = refreshTokenService.verify(refreshToken)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token"));

        String newAccessToken = jwtUtil.generateToken(token.getUser());

        return toAuthResponse(newAccessToken, refreshToken);
    }

    public void logout(String token) {
        Date expiration = jwtUtil.extractExpiration(token);

        blacklistedTokenService.add(token, expiration.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());

        String username = jwtUtil.extractUsername(token);

        User user = userRepository.findById(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        refreshTokenService.deleteByUser(user);
    }

    public AuthResponse toAuthResponse(String accessToken, String refreshToken) {
        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
