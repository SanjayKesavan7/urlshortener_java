package com.sanjayRoshan.urlshortener.domain.model;

//command object to pass the form details from controller to the service layer
public record CreateShortUrlCmd(String originalUrl,Boolean isPrivate,Integer expirationInDays,Long userId) {
}
