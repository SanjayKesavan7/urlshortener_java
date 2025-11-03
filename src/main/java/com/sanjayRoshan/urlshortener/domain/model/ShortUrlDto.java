package com.sanjayRoshan.urlshortener.domain.model;

import java.io.Serializable;
import java.time.Instant;

//for transfer of data without revealing confidential info

public record ShortUrlDto(Long id, String shortKey, String originalUrl,
                          Boolean isPrivate, Instant expiresAt,
                          UserDto createdBy, Long clickCount,
                          Instant createdAt) implements Serializable {
}