package com.sanjayRoshan.urlshortener.web;

import com.sanjayRoshan.urlshortener.domain.model.exceptions.ShortUrlNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice

public class GlobalUrlExceptionHandler {

    @ExceptionHandler(ShortUrlNotFoundException.class)
    String handleShortUrlNotFoundException(ShortUrlNotFoundException e){
        return "error/404";
    }
}
