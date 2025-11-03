package com.sanjayRoshan.urlshortener.domain.model;

import java.io.Serializable;

//for transfer of data without revealing confidential info


public record UserDto(Long id, String name) implements Serializable {
}