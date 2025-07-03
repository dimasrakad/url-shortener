package com.study.url_shortener.models.shorturl;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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

    @Pattern(regexp = "^[A-Za-z0-9]*$")
    private String customCode;

    private String expiredAt;
}
