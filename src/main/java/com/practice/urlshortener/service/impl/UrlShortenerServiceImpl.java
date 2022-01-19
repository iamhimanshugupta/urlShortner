package com.practice.urlshortener.service.impl;

import com.practice.urlshortener.annotation.Log;
import com.practice.urlshortener.entity.TinyUrls;
import com.practice.urlshortener.exception.UrlShortenerException;
import com.practice.urlshortener.model.ErrorCode;
import com.practice.urlshortener.model.Id;
import com.practice.urlshortener.model.Url;
import com.practice.urlshortener.repository.UrlShortenerRepository;
import com.practice.urlshortener.service.UrlShortenerService;
import com.practice.urlshortener.util.HelperFunctions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UrlShortenerServiceImpl implements UrlShortenerService {

    @Autowired
    private UrlShortenerRepository urlShortenerRepository;

    @Override
    @Log(name = "CREATE-TINY-URL")
    public ResponseEntity<Id> createTinyUrl(Url url) {

        TinyUrls tinyUrls = urlShortenerRepository.getTinyUrl(url.getUrl(), Timestamp.valueOf(LocalDateTime.now()));

        if (tinyUrls == null) {
            tinyUrls = new TinyUrls();
            tinyUrls.setId(HelperFunctions.randomAlphanumeric(6));
            tinyUrls.setUrl(url.getUrl());
            tinyUrls.setExpiryDateTime(Timestamp.valueOf(LocalDateTime.now().plusDays(1)));

            urlShortenerRepository.save(tinyUrls);
        }

        return new ResponseEntity<>(new Id(tinyUrls.getId()), HttpStatus.CREATED);
    }

    @Override
    @Log(name = "GET-COMPLETE-URL")
    public ResponseEntity<Url> getCompleteUrl(String id) {

        Optional<TinyUrls> tinyUrls = urlShortenerRepository.findById(id);

        if (!tinyUrls.isPresent()) {
            throw new UrlShortenerException(ErrorCode.INVALID_ID);
        }

        return new ResponseEntity<>(new Url(tinyUrls.get().getUrl()), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<TinyUrls>> getAllUrls(Integer pageNo) {

        Pageable firstPageWithTenElements = PageRequest.of(pageNo - 1, 10, Sort.by("expiryDateTime"));

        Page<TinyUrls> tinyUrlsPage = urlShortenerRepository.findAll(firstPageWithTenElements);

        return new ResponseEntity<>(tinyUrlsPage.stream().collect(Collectors.toList()), HttpStatus.OK);
    }
}
