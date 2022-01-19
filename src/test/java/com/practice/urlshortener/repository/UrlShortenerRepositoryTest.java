package com.practice.urlshortener.repository;

import com.practice.urlshortener.entity.TinyUrls;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UrlShortenerRepositoryTest {

    @Autowired
    private UrlShortenerRepository urlShortenerRepository;

    @BeforeAll
    public void beforeAll() {
        urlShortenerRepository.save(new TinyUrls("QvVyLN", "https://kernelbit.com", Timestamp.valueOf(LocalDateTime.parse("2022-01-14T05:35:03.418"))));
        urlShortenerRepository.save(new TinyUrls("H3ZgRt", "https://himanshugupta.me", Timestamp.valueOf(LocalDateTime.parse("2022-01-15T05:35:03.418"))));
    }

    @Test
    void checkIfUrlExists() {

        String url = "https://kernelbit.com";
        Timestamp timestamp = Timestamp.valueOf(LocalDateTime.parse("2022-01-13T05:35:03.418"));

        Boolean ifUrlExists = urlShortenerRepository.checkIfUrlExists(url, timestamp);

        assertEquals(true, ifUrlExists);
    }

    @Test
    void getTinyUrl() {

        String url = "https://himanshugupta.me";
        Timestamp timestamp = Timestamp.valueOf(LocalDateTime.parse("2022-01-14T05:35:03.418"));

        TinyUrls tinyUrls = urlShortenerRepository.getTinyUrl(url, timestamp);

        assertEquals("H3ZgRt", tinyUrls.getId());
        assertEquals(url, tinyUrls.getUrl());
    }

    @AfterAll
    public void afterAll() {
        urlShortenerRepository.deleteAll();
    }
}