package com.sanjayRoshan.urlshortener.domain.model;

import java.io.Serializable;


public record UserDto(Long id, String name) implements Serializable {
}