package com.practice.urlshortener.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name = "TINY_URLS")
@Data
public class TinyUrls {

    @Id
    @Column(name = "ID")
    private String Id;
    @Column(name = "URL")
    private String url;
    @Column(name = "EXPIRY_DATE_TIME", columnDefinition = "TIMESTAMP(3) NOT NULL")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Timestamp expiryDateTime;

    public TinyUrls() {
    }

    public TinyUrls(String id, String url, Timestamp expiryDateTime) {
        Id = id;
        this.url = url;
        this.expiryDateTime = expiryDateTime;
    }
}
