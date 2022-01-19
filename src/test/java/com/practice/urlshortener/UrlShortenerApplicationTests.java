package com.practice.urlshortener;

import com.practice.urlshortener.entity.TinyUrls;
import com.practice.urlshortener.model.Id;
import com.practice.urlshortener.model.Url;
import com.practice.urlshortener.repository.UrlShortenerRepository;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UrlShortenerApplicationTests {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @LocalServerPort
    private int port;

    @Autowired
    private UrlShortenerRepository urlShortenerRepository;

    @Test
    void contextLoads() {
    }

    @Test
    @Tag("INTEGRATION_TEST")
    public void integrationTest() throws URISyntaxException {

        String url = "https://google.com";

        //Step 1: Create Tiny Url
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Url> urlHttpEntity = new HttpEntity<>(new Url(url), headers);

        Id id = this.testRestTemplate.postForObject("/create/tiny/url", urlHttpEntity, Id.class);

        //Check 1: If Url exists in DB or not
        Optional<TinyUrls> tinyUrlsOptional = urlShortenerRepository.findById(id.getId());
        assertEquals(true, tinyUrlsOptional.isPresent());

        //Check 2: Expiry Date should be one day ahead
        Timestamp currentTimeStamp = Timestamp.valueOf(LocalDateTime.now());
        assertEquals(1, tinyUrlsOptional.get().getExpiryDateTime().compareTo(currentTimeStamp));

        //Step 2: Get Complete Url
        HttpEntity idHttpEntity = new HttpEntity<>(headers);

        String getCompleteUrl = UriComponentsBuilder
                .fromHttpUrl("http://localhost:" + port + "/url-shortener/get/complete/url")
                .queryParam("id", id.getId())
                .encode()
                .toUriString();
        ResponseEntity<Url> urlResponseEntity = this.testRestTemplate.exchange(getCompleteUrl, HttpMethod.GET, idHttpEntity, Url.class);

        assertEquals(url, urlResponseEntity.getBody().getUrl());

        //Step 3: Get all Urls
        HttpEntity getAllUrlsHttpEntity = new HttpEntity(headers);

        String getAllUrls = UriComponentsBuilder
                .fromUri(new URI("/get/all/urls"))
                .queryParam("pageNo", 1)
                .encode()
                .toUriString();

        ResponseEntity<List<TinyUrls>> listResponseEntity = this.testRestTemplate.exchange(getAllUrls, HttpMethod.GET, getAllUrlsHttpEntity, new ParameterizedTypeReference<List<TinyUrls>>() {
        });

        //TODO Ask Bhasker Sir
        assertEquals(Collections.singletonList(tinyUrlsOptional.get()), listResponseEntity.getBody());
    }

}
