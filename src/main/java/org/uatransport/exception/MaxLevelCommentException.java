package org.uatransport.exception;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MaxLevelCommentException extends RuntimeException {

    public MaxLevelCommentException(String message) {
        super(message);
    }
}
