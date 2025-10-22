package com.sanjayRoshan.urlshortener.domain.service;

import com.sanjayRoshan.urlshortener.domain.entities.ShortUrl;
import com.sanjayRoshan.urlshortener.domain.repository.ShortUrlRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShortUrlService {
    private final ShortUrlRepository shortUrlRepository;
    public ShortUrlService(ShortUrlRepository shortUrlRepository){
        this.shortUrlRepository=shortUrlRepository;
    }
    public List<ShortUrl> findAllPublicShortUrls(){
        return shortUrlRepository.findPublicShortUrls();
    }
}
