package com.study.url_shortener.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.study.url_shortener.models.WebResponse;
import com.study.url_shortener.models.shorturl.CreateShortUrlRequest;
import com.study.url_shortener.models.shorturl.ShortUrlResponse;
import com.study.url_shortener.services.ShortUrlService;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/url")
public class ShortUrlController {
    @Autowired
    private ShortUrlService shortUrlService;

    @PostMapping()
    public WebResponse<ShortUrlResponse> create(@RequestBody CreateShortUrlRequest request,
            Authentication authentication, HttpServletRequest httpServletRequest) {
        ShortUrlResponse response = shortUrlService.create(request, authentication, httpServletRequest);

        return WebResponse.<ShortUrlResponse>builder()
                .data(response)
                .build();
    }

    @GetMapping("/{shortCode}")
    public WebResponse<ShortUrlResponse> getByCode(@PathVariable String shortCode, Authentication authentication,
            HttpServletRequest httpServletRequest) {
        ShortUrlResponse response = shortUrlService.getByCode(shortCode, authentication, httpServletRequest);

        return WebResponse.<ShortUrlResponse>builder()
                .data(response)
                .build();
    }

    @GetMapping()
    public WebResponse<List<ShortUrlResponse>> getAll(Authentication authentication,
            HttpServletRequest httpServletRequest) {
        List<ShortUrlResponse> responses = shortUrlService.getAll(authentication, httpServletRequest);

        return WebResponse.<List<ShortUrlResponse>>builder()
                .data(responses)
                .build();
    }

    @DeleteMapping("/{shortCode}")
    public WebResponse<String> delete(@PathVariable String shortCode, Authentication authentication) {
        shortUrlService.delete(shortCode, authentication);

        return WebResponse.<String>builder()
                .data(null)
                .build();
    }

}
