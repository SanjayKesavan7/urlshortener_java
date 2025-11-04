package com.sanjayRoshan.urlshortener.domain.model;

public record CreateUserCmd(
        String email,
        String password,
        String name,
        Role role) {
}