package com.study.url_shortener.controllers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.study.url_shortener.services.ShortUrlService;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
public class RedirectController {
    @Autowired
    private ShortUrlService shortUrlService;

    @GetMapping("/{shortCode}")
    public void redirectToOriginal(@PathVariable String shortCode, HttpServletResponse response) {
        String originalUrl = shortUrlService.getOriginalUrl(shortCode);

        try {
            response.sendRedirect(originalUrl);

            shortUrlService.incrementHitCount(shortCode);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to redirect to original URL");
        }
    }
    
}
