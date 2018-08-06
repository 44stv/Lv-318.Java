package org.uatransport.exception;

import org.springframework.http.HttpStatus;

public class BadWordFoundException extends RuntimeException {

    private final String message;
    private final HttpStatus httpStatus;

    public BadWordFoundException (String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
