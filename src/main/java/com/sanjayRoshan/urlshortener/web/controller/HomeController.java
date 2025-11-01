package com.sanjayRoshan.urlshortener.web.controller;

import com.sanjayRoshan.urlshortener.ApplicationProperties;
import com.sanjayRoshan.urlshortener.domain.entities.ShortUrl;
import com.sanjayRoshan.urlshortener.domain.entities.User;
import com.sanjayRoshan.urlshortener.domain.model.CreateShortUrlCmd;
import com.sanjayRoshan.urlshortener.domain.model.ShortUrlDto;
import com.sanjayRoshan.urlshortener.domain.model.exceptions.ShortUrlNotFoundException;
import com.sanjayRoshan.urlshortener.domain.repository.ShortUrlRepository;
import com.sanjayRoshan.urlshortener.domain.service.ShortUrlService;
import com.sanjayRoshan.urlshortener.web.dtos.CreateShortUrlForm;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
public class HomeController {
    private final ShortUrlService shortUrlService;
    private final ApplicationProperties properties;
    private final SecurityUtils securityUtils;

    public HomeController(ShortUrlService shortUrlService, ApplicationProperties properties,SecurityUtils securityUtils) {
        this.shortUrlService = shortUrlService;
        this.properties = properties;
        this.securityUtils=securityUtils;
    }

    @GetMapping("/")
    public String home(Model model) {
        User currentUser = securityUtils.getCurrentUser();
        List<ShortUrlDto> shortUrls = shortUrlService.findAllPublicShortUrls();
        model.addAttribute("shortUrls", shortUrls);
        model.addAttribute("baseUrl", properties.baseUrl());
        model.addAttribute("createShortUrlForm", new CreateShortUrlForm(""));
        return "index";
    }

    @PostMapping("/short-urls")
    String createShortUrl(@ModelAttribute("createShortUrlForm") @Valid CreateShortUrlForm form,
                          BindingResult bindingResult,
                          RedirectAttributes redirectAttributes,
                          Model model) {
        if(bindingResult.hasErrors()) {
            List<ShortUrlDto> shortUrls = shortUrlService.findAllPublicShortUrls();
            model.addAttribute("shortUrls", shortUrls);
            model.addAttribute("baseUrl", properties.baseUrl());
            return "index";
        }

        try {
            CreateShortUrlCmd cmd = new CreateShortUrlCmd(form.originalUrl());
            var shortUrlDto = shortUrlService.createShortUrl(cmd);
            redirectAttributes.addFlashAttribute("successMessage", "Short URL created successfully "+
                    properties.baseUrl()+"/s/"+shortUrlDto.shortKey());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to create short URL");

        }
        return "redirect:/";
    }
        @GetMapping("/s/{shortKey}")
        String redirectToOriginalUrl(@PathVariable String shortKey) throws ShortUrlNotFoundException{
            Optional<ShortUrlDto> shortUrlDtoOptional = shortUrlService.accessShortUrl(shortKey);
            if(shortUrlDtoOptional.isEmpty()){
                throw new ShortUrlNotFoundException("Url NOt found");
            }
            ShortUrlDto shortUrlDto = shortUrlDtoOptional.get();
            return "redirect:"+shortUrlDto.originalUrl();
        }

        @GetMapping("/login")
        String loginForm(){
        return "login";
        }
}