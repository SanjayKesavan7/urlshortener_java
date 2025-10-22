package com.sanjayRoshan.urlshortener.controller;

import com.sanjayRoshan.urlshortener.domain.entities.ShortUrl;
import com.sanjayRoshan.urlshortener.domain.repository.ShortUrlRepository;
import com.sanjayRoshan.urlshortener.domain.service.ShortUrlService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {
    private final ShortUrlService shortUrlService;

    public HomeController(ShortUrlService shortUrlService){
        this.shortUrlService = shortUrlService;
    }


    @GetMapping("/")
    public String home(Model model){
        List<ShortUrl>shorturls = shortUrlService.findAllPublicShortUrls();
        model.addAttribute("shortUrls",shorturls);
        model.addAttribute("title","hello world");
        return "index";
    }

}
