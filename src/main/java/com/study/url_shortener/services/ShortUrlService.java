package com.study.url_shortener.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.study.url_shortener.repositories.ShortUrlRepository;

@Service
public class ShortUrlService {
    @Autowired
    private ShortUrlRepository shortUrlRepository;
}
