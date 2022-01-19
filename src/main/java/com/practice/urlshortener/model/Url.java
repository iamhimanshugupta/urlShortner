package com.practice.urlshortener.model;

import lombok.Data;
import org.hibernate.validator.constraints.URL;

@Data
public class Url {

    @URL(message = "URL is not of valid type")
    private String url;

    public Url() {
    }

    public Url(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Url{" +
                "url='" + url + '\'' +
                '}';
    }
}
