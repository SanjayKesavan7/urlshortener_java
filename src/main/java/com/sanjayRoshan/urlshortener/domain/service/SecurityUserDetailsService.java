package com.sanjayRoshan.urlshortener.domain.service;

import com.sanjayRoshan.urlshortener.domain.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SecurityUserDetailsService implements UserDetailsService {
    private final UserRepository userRepostiory;

    public SecurityUserDetailsService(UserRepository userRepostiory) {
        this.userRepostiory = userRepostiory;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       com.sanjayRoshan.urlshortener.domain.entities.User user = userRepostiory.findByEmail(username)
                .orElseThrow(
                        ()-> new UsernameNotFoundException("User not found with email!!")
                );
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),user.getPassword(), List.of(new SimpleGrantedAuthority(user.getRole().name()))
        );
    }
}
