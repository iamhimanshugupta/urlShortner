package com.practice.urlshortener.controller;

import com.practice.urlshortener.entity.TinyUrls;
import com.practice.urlshortener.model.Id;
import com.practice.urlshortener.model.Url;
import com.practice.urlshortener.service.UrlShortenerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@Validated
public class UrlShortenerController {

    @Autowired
    private UrlShortenerService urlShortenerService;

    @PostMapping("/create/tiny/url")
    public ResponseEntity<Id> createTinyUrl(@Valid @RequestBody Url url) {

        return urlShortenerService.createTinyUrl(url);
    }

    @GetMapping("/get/complete/url")
    public ResponseEntity<Url> getCompleteUrl(@NotBlank(message = "ID is required") @RequestParam(name = "id") String id) {
        return urlShortenerService.getCompleteUrl(id);
    }

    @GetMapping("/get/all/urls")
    public ResponseEntity<List<TinyUrls>> getAllUrls(@Valid @Min(value = 1, message = "Invalid Page Number") @Max(value = Integer.MAX_VALUE, message = "Invalid Page Number") @NotNull(message = "Page number is required") @RequestParam(name = "pageNo") Integer pageNo) {

        return urlShortenerService.getAllUrls(pageNo);
    }
}
