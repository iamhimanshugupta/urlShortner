package com.practice.urlshortener.exception;

import com.practice.urlshortener.model.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

@ControllerAdvice
public class ExceptionController {

    private static final Logger LOGGER = LoggerFactory.getLogger("COMMON_LOGGER");

    @ExceptionHandler(UrlShortenerException.class)
    public ResponseEntity<ErrorResponse> handleUrlShortenerException(
            UrlShortenerException urlShortenerException) {

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorCode(urlShortenerException.getErrorCode());
        errorResponse.setErrorMsg(urlShortenerException.getErrorMsg());

        LOGGER.error(errorResponse.toString());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException methodArgumentNotValidException) {

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorCode("MANV");
        errorResponse.setErrorMsg(methodArgumentNotValidException.getBindingResult().getFieldError().getDefaultMessage());

        LOGGER.error(errorResponse.toString());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException constraintViolationException) {

        Set<ConstraintViolation<?>> violations = constraintViolationException.getConstraintViolations();

        StringBuilder strBuilder = new StringBuilder();
        for (ConstraintViolation<?> violation : violations) {
            strBuilder.append(violation.getMessage());
            break;
        }

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorCode("CVE");
        errorResponse.setErrorMsg(strBuilder.toString());

        LOGGER.error(errorResponse.toString());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException httpMessageNotReadableException) {

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorCode("EX");
        String errMsg = httpMessageNotReadableException.getMessage();
        errMsg = errMsg.substring(0, errMsg.indexOf(":"));
        errorResponse.setErrorMsg(errMsg);

        LOGGER.error(errorResponse.toString());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception exception) {

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorCode("EX");
        errorResponse.setErrorMsg(exception.getMessage());

        LOGGER.error(errorResponse.toString());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
