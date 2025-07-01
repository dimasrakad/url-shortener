package com.study.url_shortener.models.shorturl;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateShortUrlRequest {
    @NotBlank
    private String originalUrl;

    private String customCode;

    private String expiredAt;
}
