package com.study.url_shortener.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.study.url_shortener.entities.BlacklistedToken;

@Repository
public interface BlacklistedTokenRepository extends JpaRepository<BlacklistedToken, String> {
    List<BlacklistedToken> findByExpiryDateBefore(LocalDateTime now);
}
