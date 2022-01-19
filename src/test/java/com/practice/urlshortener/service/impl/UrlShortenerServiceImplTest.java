package com.practice.urlshortener.service.impl;

import com.practice.urlshortener.entity.TinyUrls;
import com.practice.urlshortener.exception.UrlShortenerException;
import com.practice.urlshortener.model.Id;
import com.practice.urlshortener.model.Url;
import com.practice.urlshortener.repository.UrlShortenerRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UrlShortenerServiceImplTest {

    @Mock
    private UrlShortenerRepository urlShortenerRepository;

    @InjectMocks
    private UrlShortenerServiceImpl urlShortenerService;

    @Captor
    private ArgumentCaptor<TinyUrls> tinyUrlsArgumentCaptor;

    @Nested
    public class CreateTinyUrl {

        @Test
        public void testWithUrlNotPresentInDb() {
            when(urlShortenerRepository.getTinyUrl(anyString(), any(Timestamp.class))).thenReturn(null);

            ResponseEntity<Id> idResponseEntity = urlShortenerService.createTinyUrl(new Url("https://kernelbit.com"));

            verify(urlShortenerRepository).save(tinyUrlsArgumentCaptor.capture());

            ResponseEntity<Id> expectedResponseEntity = new ResponseEntity<>(new Id(tinyUrlsArgumentCaptor.getValue().getId()), HttpStatus.CREATED);

            assertEquals(expectedResponseEntity, idResponseEntity);
        }

        @Test
        public void testWithUrlPresentInDb() {
            when(urlShortenerRepository.getTinyUrl(anyString(), any(Timestamp.class))).thenReturn(new TinyUrls("QvVyLN", "https://kernelbit.com", Timestamp.valueOf(LocalDateTime.now())));

            ResponseEntity<Id> idResponseEntity = urlShortenerService.createTinyUrl(new Url("https://kernelbit.com"));

            ResponseEntity<Id> expectedResponseEntity = new ResponseEntity<>(new Id(idResponseEntity.getBody().getId()), HttpStatus.CREATED);

            assertEquals(expectedResponseEntity, idResponseEntity);
        }
    }

    @Nested
    public class GetCompleteUrl {

        @Test
        public void testWithUrlNotPresentInDb() {

            when(urlShortenerRepository.findById(anyString())).thenReturn(Optional.ofNullable(null));

            assertThrows(UrlShortenerException.class, () -> urlShortenerService.getCompleteUrl(anyString()));
        }

        @Test
        public void testWithUrlPresentInDb() {
            when(urlShortenerRepository.findById(anyString())).thenReturn(Optional.of(new TinyUrls("QvVyLN", "https://kernelbit.com", Timestamp.valueOf(LocalDateTime.now()))));

            ResponseEntity<Url> urlResponseEntity = urlShortenerService.getCompleteUrl(anyString());

            ResponseEntity<Url> expectedResponseEntity = new ResponseEntity<>(new Url(urlResponseEntity.getBody().getUrl()), HttpStatus.OK);

            assertEquals(expectedResponseEntity, urlResponseEntity);
        }
    }

    @Test
    void getAllUrls() {

        when(urlShortenerRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(Collections.emptyList()));

        ResponseEntity<List<TinyUrls>> listResponseEntity = urlShortenerService.getAllUrls(anyInt() + 1);

        ResponseEntity<List<TinyUrls>> expected = new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);

        assertEquals(expected, listResponseEntity);
    }
}