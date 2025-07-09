package com.study.url_shortener.models.shorturl;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShortUrlResponse {
    private String shortUrl;
    private String originalUrl;
    private String expiredAt;
    private Integer hitCount;
    private String deletedAt;
    private String username;
}
