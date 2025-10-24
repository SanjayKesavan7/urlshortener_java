package com.sanjayRoshan.urlshortener.domain.model.exceptions;

public class ShortUrlNotFoundException extends Exception{
    public ShortUrlNotFoundException(String message) {
        super(message);
    }
}
