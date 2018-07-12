package org.uatransport.exception;

public class UserValidationException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private final String message;


    public UserValidationException(String message) {
        this.message = message;

    }

    @Override
    public String getMessage() {
        return message;
    }

}
