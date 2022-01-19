package com.practice.urlshortener.service;

import com.practice.urlshortener.entity.TinyUrls;
import com.practice.urlshortener.model.Id;
import com.practice.urlshortener.model.Url;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UrlShortenerService {

    ResponseEntity<Id> createTinyUrl(Url url);

    ResponseEntity<Url> getCompleteUrl(String id);

    ResponseEntity<List<TinyUrls>> getAllUrls(Integer pageNo);
}
