package com.study.url_shortener.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.study.url_shortener.models.RefreshTokenRequest;
import com.study.url_shortener.models.WebResponse;
import com.study.url_shortener.models.user.AuthRequest;
import com.study.url_shortener.models.user.AuthResponse;
import com.study.url_shortener.services.RefreshTokenService;
import com.study.url_shortener.services.UserService;
import com.study.url_shortener.utils.JwtUtil;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/login")
    public WebResponse<AuthResponse> login(@RequestBody AuthRequest authRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getUsername(),
                            authRequest.getPassword()));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
        }

        UserDetails userDetails = userService.loadUserByUsername(authRequest.getUsername());
        String accessToken = jwtUtil.generateToken(userDetails);
        String refreshToken = refreshTokenService.create(userDetails.getUsername());

        return WebResponse.<AuthResponse>builder()
                .data(userService.toAuthResponse(accessToken, refreshToken)).build();
    }

    @PostMapping("/register")
    public WebResponse<String> register(@RequestBody AuthRequest authRequest) {
        userService.register(authRequest);

        return WebResponse.<String>builder().data(null).build();
    }

    @PostMapping("/refresh")
    public WebResponse<AuthResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        AuthResponse response = userService.refreshAccessToken(request.getRefreshToken());

        return WebResponse.<AuthResponse>builder().data(response).build();
    }

}
