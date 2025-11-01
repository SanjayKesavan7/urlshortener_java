package com.sanjayRoshan.urlshortener.domain.service;

import com.sanjayRoshan.urlshortener.ApplicationProperties;
import com.sanjayRoshan.urlshortener.domain.entities.ShortUrl;
import com.sanjayRoshan.urlshortener.domain.model.CreateShortUrlCmd;
import com.sanjayRoshan.urlshortener.domain.model.ShortUrlDto;
import com.sanjayRoshan.urlshortener.domain.repository.ShortUrlRepository;
import com.sanjayRoshan.urlshortener.domain.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static java.time.temporal.ChronoUnit.*;

@Service
@Transactional(readOnly = true)
public class ShortUrlService {

    private final ShortUrlRepository shortUrlRepository;
    private final EntityMapper entityMapper;
    private final ApplicationProperties properties;
    private final UserRepository userRepository;

    public ShortUrlService(EntityMapper entityMapper, ShortUrlRepository shortUrlRepository, ApplicationProperties properties, UserRepository userRepository) {
        this.entityMapper = entityMapper;
        this.shortUrlRepository = shortUrlRepository;
        this.properties = properties;
        this.userRepository = userRepository;
    }



    public List<ShortUrlDto> findAllPublicShortUrls() {
        return shortUrlRepository.findPublicShortUrls()
                .stream().map(entityMapper::toShortUrlDto).toList();
    }

    @Transactional
    public ShortUrlDto createShortUrl(CreateShortUrlCmd cmd) {
        String verUrl = cmd.originalUrl();
        if(!(verUrl.substring(0,7).equals("http://"))){
            verUrl = "http://"+cmd.originalUrl();
        }
        if(properties.validateOriginalUrl()) {
            boolean urlExists = UrlExistenceValidator.isUrlExists(verUrl);
            if(!urlExists) {
                throw new RuntimeException("Invalid URL "+cmd.originalUrl());
            }
        }
        var shortKey = generateUniqueShortKey();
        var shortUrl = new ShortUrl();
        shortUrl.setOriginalUrl(verUrl);
        shortUrl.setShortKey(shortKey);
        if(cmd.userId()==null){
            shortUrl.setCreatedBy(null);
            shortUrl.setIsPrivate(false);
            shortUrl.setExpiresAt(Instant.now().plus(properties.defaultExpiryInDays(), DAYS));
        }
        else {
            shortUrl.setCreatedBy(userRepository.findById(cmd.userId()).orElseThrow());
            shortUrl.setIsPrivate(cmd.isPrivate() == null?false:cmd.isPrivate());
            shortUrl.setExpiresAt(cmd.expirationInDays()!=null?Instant.now().plus(cmd.expirationInDays(),DAYS):null);
        }

        shortUrl.setClickCount(0L);
        shortUrl.setExpiresAt(Instant.now().plus(properties.defaultExpiryInDays(), DAYS));
        shortUrl.setCreatedAt(Instant.now());
        shortUrlRepository.save(shortUrl);
        return entityMapper.toShortUrlDto(shortUrl);
    }

    private String generateUniqueShortKey() {
        String shortKey;
        do {
            shortKey = generateRandomShortKey();
        } while (shortUrlRepository.existsByShortKey(shortKey));
        return shortKey;
    }

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int SHORT_KEY_LENGTH = 6;
    private static final SecureRandom RANDOM = new SecureRandom();

    public static String generateRandomShortKey() {
        StringBuilder sb = new StringBuilder(SHORT_KEY_LENGTH);
        for (int i = 0; i < SHORT_KEY_LENGTH; i++) {
            sb.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }
    @Transactional
    public Optional<ShortUrlDto> accessShortUrl(String shortKey){
        Optional<ShortUrl> optionalShortUrl = shortUrlRepository.findByShortKey(shortKey);
        if(optionalShortUrl.isEmpty()){
            return Optional.empty();
        }
        ShortUrl shortUrl = optionalShortUrl.get();
        if(shortUrl.getExpiresAt()!=null && shortUrl.getExpiresAt().isBefore(Instant.now())){
            return Optional.empty();
        }
        shortUrl.setClickCount(shortUrl.getClickCount()+1);
        shortUrlRepository.save(shortUrl);
        return optionalShortUrl.map(entityMapper::toShortUrlDto);
    }
}