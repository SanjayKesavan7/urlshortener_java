package com.sanjayRoshan.urlshortener.web.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

//form object where the details of the form are put to this object

public record CreateShortUrlForm(
        @NotBlank(message = "Original URL is required")
        String originalUrl,
        Boolean isPrivate,
        @Min(1)
        @Max(365)
        Integer expirationInDays) {
}