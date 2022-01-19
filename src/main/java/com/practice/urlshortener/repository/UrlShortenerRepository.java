package com.practice.urlshortener.repository;

import com.practice.urlshortener.entity.TinyUrls;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;

public interface UrlShortenerRepository extends JpaRepository<TinyUrls, String> {

    @Query("SELECT CASE WHEN COUNT(1) > 0 THEN TRUE ELSE FALSE END FROM TinyUrls WHERE URL = :url AND :dateTime < EXPIRY_DATE_TIME")
    Boolean checkIfUrlExists(@Param("url") String url, @Param("dateTime") Timestamp timestamp);

    @Query("SELECT tinyUrls FROM TinyUrls tinyUrls WHERE URL = :url AND :dateTime < EXPIRY_DATE_TIME")
    TinyUrls getTinyUrl(@Param("url") String url, @Param("dateTime") Timestamp timestamp);
}
