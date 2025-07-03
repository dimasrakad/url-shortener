package com.study.url_shortener.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.study.url_shortener.entities.ShortUrl;
import com.study.url_shortener.entities.User;

@Repository
public interface ShortUrlRepository extends JpaRepository<ShortUrl, String> {
    List<ShortUrl> findByUser(User user);
    Optional<ShortUrl> findByShortCodeAndDeletedAtIsNull(String shortCode);
    Optional<ShortUrl> findByShortCodeAndUser(String shortCode, User user);
}
