package org.uatransport.exception;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TimeExpiredException extends RuntimeException {

    public TimeExpiredException(String message) {
        super(message);
    }
}
