package com.practice.urlshortener.model;

import lombok.Data;

@Data
public class UrlShortenerLogging {

    /**
     * Application Name
     */
    private String a;
    /**
     * Event Name
     */
    private String e;
    /**
     * Unique Id
     */
    private String u;
    /**
     * Request
     */
    private String req;
    /**
     * Response
     */
    private String res;
    /**
     * Time Taken to process in milliseconds
     */
    private Long t;

    @Override
    public String toString() {
        return "UrlShortenerLogging{" +
                "a='" + a + '\'' +
                ", e='" + e + '\'' +
                ", u=" + u +
                ", req='" + req + '\'' +
                ", res='" + res + '\'' +
                ", t=" + t +
                '}';
    }
}
