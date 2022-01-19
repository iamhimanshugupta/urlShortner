package com.practice.urlshortener.exception;

import com.practice.urlshortener.model.ErrorCode;
import lombok.Data;

@Data
public class UrlShortenerException extends RuntimeException {

    private String errorCode;
    private String errorMsg;

    public UrlShortenerException(String errorCode, String errorMsg) {
        super(errorMsg);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public UrlShortenerException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode.name();
        this.errorMsg = errorCode.getMessage();
    }

    public UrlShortenerException(String errorMsg) {
        super(errorMsg);
        this.errorMsg = errorMsg;
    }

}
