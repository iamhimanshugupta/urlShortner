package com.practice.urlshortener.model;

public enum ErrorCode {

    URL_EXITS("Provided URL already exists"),
    INVALID_ID("Provided ID doesn't exists");

    private String msg;

    ErrorCode(String msg) {
        this.msg = msg;
    }

    public String getMessage() {
        return this.msg;
    }
}
