package com.sanjayRoshan.urlshortener.domain.model;

public record CreateShortUrlCmd(String originalUrl,Boolean isPrivate,Integer expirationInDays,Long userId) {
}
