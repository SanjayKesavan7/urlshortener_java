package com.sanjayRoshan.urlshortener.domain.service;

import com.sanjayRoshan.urlshortener.ApplicationProperties;
import com.sanjayRoshan.urlshortener.domain.entities.ShortUrl;
import com.sanjayRoshan.urlshortener.domain.model.CreateShortUrlCmd;
import com.sanjayRoshan.urlshortener.domain.model.PagedResult;
import com.sanjayRoshan.urlshortener.domain.model.ShortUrlDto;
import com.sanjayRoshan.urlshortener.domain.repository.ShortUrlRepository;
import com.sanjayRoshan.urlshortener.domain.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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



    public PagedResult<ShortUrlDto> findAllPublicShortUrls(int pageNo, int pageSize) {
        Pageable pageable = getPageable(pageNo, pageSize);
        Page<ShortUrlDto> shortUrlDtoPage = shortUrlRepository.findPublicShortUrls(pageable)
                .map(entityMapper::toShortUrlDto);
        return PagedResult.from(shortUrlDtoPage);
    }

    public PagedResult<ShortUrlDto> getUserShortUrls(Long userId, int page, int pageSize) {
        Pageable pageable = getPageable(page, pageSize);
        var shortUrlsPage = shortUrlRepository.findByCreatedById(userId, pageable)
                .map(entityMapper::toShortUrlDto);
        return PagedResult.from(shortUrlsPage);
    }

    @Transactional
    public void deleteUserShortUrls(List<Long> ids, Long userId) {
        if (ids != null && !ids.isEmpty() && userId != null) {
            shortUrlRepository.deleteByIdInAndCreatedById(ids, userId);
        }
    }

    public PagedResult<ShortUrlDto> findAllShortUrls(int page, int pageSize) {
        Pageable pageable = getPageable(page, pageSize);
        var shortUrlsPage =  shortUrlRepository.findAllShortUrls(pageable).map(entityMapper::toShortUrlDto);
        return PagedResult.from(shortUrlsPage);
    }

    private Pageable getPageable(int page, int size) {
        page = page > 1 ? page - 1: 0;
        return PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
    }
    // validates and generates the short url for the original entered url
    @Transactional
    public ShortUrlDto createShortUrl(CreateShortUrlCmd cmd) {
        String verUrl = cmd.originalUrl().trim();
        if (!verUrl.startsWith("http://") && !verUrl.startsWith("https://")) {
            verUrl = "http://" + verUrl;
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

    //helper function to generate a unique short key of 6 characters
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

    //generate a random key which the unique key generator uses
    public static String generateRandomShortKey() {
        StringBuilder sb = new StringBuilder(SHORT_KEY_LENGTH);
        for (int i = 0; i < SHORT_KEY_LENGTH; i++) {
            sb.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }

    //finds the short key from the database and returns the short url dto
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