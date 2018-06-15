package org.uatransport.config;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.uatransport.exception.ResourceNotFoundException;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private static final HttpHeaders HTTP_HEADERS = new HttpHeaders();

    @ExceptionHandler(value = ResourceNotFoundException.class)
    protected ResponseEntity<Object> handleConflict(ResourceNotFoundException ex,
                                                    WebRequest request) {
        final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex);
        return handleExceptionInternal(ex, apiError, HTTP_HEADERS, apiError.getStatus(), request);
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex,
                                                               WebRequest request) {
        final ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex);
        apiError.setMessage("Validation error occurred: " + ex.getCause());
        return handleExceptionInternal(ex, apiError, HTTP_HEADERS, apiError.getStatus(), request);
    }

    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
                                                                      WebRequest request) {
        Class<?> requiredType = ex.getRequiredType();
        final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex);
        apiError.setMessage(String.format("The parameter '%s' of value '%s' could not be converted to type '%s'",
                ex.getName(), ex.getValue(), requiredType != null ? requiredType.getName() : "of the argument"));
        return handleExceptionInternal(ex, apiError, HTTP_HEADERS, apiError.getStatus(), request);
    }

    @ExceptionHandler(value = {IllegalArgumentException.class, IllegalStateException.class})
    protected ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {
        final ApiError apiError = new ApiError(HttpStatus.CONFLICT, ex);
        apiError.setMessage("This should be application specific");
        return handleExceptionInternal(ex, apiError, HTTP_HEADERS, apiError.getStatus(), request);
    }
}