package com.study.url_shortener.services;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.study.url_shortener.entities.ShortUrl;
import com.study.url_shortener.entities.User;
import com.study.url_shortener.models.shorturl.CreateShortUrlRequest;
import com.study.url_shortener.models.shorturl.ShortUrlResponse;
import com.study.url_shortener.repositories.ShortUrlRepository;
import com.study.url_shortener.repositories.UserRepository;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class ShortUrlService {
    @Autowired
    private ShortUrlRepository shortUrlRepository;

    @Autowired
    private UserRepository userRepository;

    public ShortUrlResponse create(CreateShortUrlRequest request, Authentication authentication,
            HttpServletRequest httpServletRequest) {
        String username = authentication.getName();

        User user = userRepository.findById(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (!isValidURL(request.getOriginalUrl())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid URL");
        }

        if (!isValidDateTime(request.getExpiredAt())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid expiredAt");
        }

        String shortCode = request.getCustomCode() != null && !request.getCustomCode().isEmpty()
                ? request.getCustomCode()
                : generateUniqueCode();

        if (shortUrlRepository.existsByShortCode(shortCode)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Custom code already exists");
        }

        ShortUrl shortUrl = new ShortUrl();
        shortUrl.setShortCode(shortCode);
        shortUrl.setOriginalUrl(request.getOriginalUrl());
        shortUrl.setExpiredAt(request.getExpiredAt() != null ? LocalDateTime.parse(request.getExpiredAt()) : null);
        shortUrl.setUser(user);

        shortUrlRepository.save(shortUrl);

        return toShortUrlResponse(shortUrl, httpServletRequest);
    }

    public void incrementHitCount(String shortCode) {
        ShortUrl shortUrl = shortUrlRepository.findByShortCodeAndDeletedAtIsNull(shortCode)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Short URL not found"));

        if (shortUrl.getExpiredAt().isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Short URL has expired");
        }

        shortUrl.setHitCount(shortUrl.getHitCount() + 1);
        shortUrlRepository.save(shortUrl);
    }

    public String getOriginalUrl(String shortCode) {
        ShortUrl shortUrl = shortUrlRepository.findByShortCodeAndDeletedAtIsNull(shortCode)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Short URL not found"));

        return shortUrl.getOriginalUrl();
    }

    public ShortUrlResponse getByCode(String shortCode, Authentication authentication,
            HttpServletRequest httpServletRequest) {
        String username = authentication.getName();

        User user = userRepository.findById(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        ShortUrl shortUrl = shortUrlRepository.findByShortCodeAndUser(shortCode, user)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Short URL not found"));

        return toShortUrlResponse(shortUrl, httpServletRequest);
    }

    public List<ShortUrlResponse> getAll(Authentication authentication, HttpServletRequest httpServletRequest) {
        String username = authentication.getName();

        User user = userRepository.findById(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        List<ShortUrl> shortUrls = shortUrlRepository.findByUser(user);

        return shortUrls.stream().map(shortUrl -> toShortUrlResponse(shortUrl, httpServletRequest))
                .collect(Collectors.toList());
    }

    public void delete(String shortCode, Authentication authentication) {
        String username = authentication.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        ShortUrl shortUrl = shortUrlRepository.findByShortCodeAndUser(shortCode, user)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Short URL not found"));

        shortUrlRepository.delete(shortUrl);
    }

    private String generateUniqueCode() {
        String code;
        do {
            code = generateRandomAlphanumeric(5);
        } while (shortUrlRepository.existsByShortCode(code));

        return code;
    }

    private ShortUrlResponse toShortUrlResponse(ShortUrl shortUrl, HttpServletRequest httpServletRequest) {
        String baseUrl = getBaseUrl(httpServletRequest);

        return ShortUrlResponse.builder()
                .shortUrl(baseUrl + shortUrl.getShortCode())
                .originalUrl(shortUrl.getOriginalUrl())
                .expiredAt(shortUrl.getExpiredAt().toString())
                .hitCount(shortUrl.getHitCount())
                .deletedAt(shortUrl.getDeletedAt() != null ? shortUrl.getDeletedAt().toString() : null)
                .build();
    }

    private boolean isValidURL(String url) {
        try {
            new URL(url).toURI();
            return true;
        } catch (MalformedURLException | URISyntaxException e) {
            return false;
        }
    }

    private boolean isValidDateTime(String dateTime) {
        try {
            LocalDateTime.parse(dateTime);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private String generateRandomAlphanumeric(int length) {
        final String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            stringBuilder.append(characters.charAt(index));
        }

        return stringBuilder.toString();
    }

    private String getBaseUrl(HttpServletRequest request) {
        String scheme = request.getScheme();
        String serverName = request.getServerName();
        int serverPort = request.getServerPort();

        return scheme + "://" + serverName + ":" + serverPort + "/";
    }
}
