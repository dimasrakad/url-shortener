package com.study.url_shortener.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.study.url_shortener.entities.ShortUrl;
import com.study.url_shortener.entities.User;

@Repository
public interface ShortUrlRepository extends JpaRepository<ShortUrl, String> {
    List<ShortUrl> findByUser(User user);
}
