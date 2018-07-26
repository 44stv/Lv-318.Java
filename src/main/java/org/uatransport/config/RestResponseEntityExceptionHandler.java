package org.uatransport.config;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.uatransport.exception.*;

@ControllerAdvice(annotations = RestController.class)
@Slf4j
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private static final HttpHeaders HTTP_HEADERS = new HttpHeaders();

    @ExceptionHandler(value = ResourceNotFoundException.class)
    protected ResponseEntity<Object> handleConflict(ResourceNotFoundException ex, WebRequest request) {
        log.error("Unable to parse data {}", ex);
        final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex);
        return handleExceptionInternal(ex, apiError, HTTP_HEADERS, apiError.getStatus(), request);
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex, WebRequest request) {
        log.error("Validation error occurred:", ex.getCause());
        final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex);
        apiError.setMessage("Validation error occurred: " + ex.getCause());
        return handleExceptionInternal(ex, apiError, HTTP_HEADERS, apiError.getStatus(), request);
    }

    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
            WebRequest request) {
        log.error("Unable to parse data {}", ex);
        Class<?> requiredType = ex.getRequiredType();
        final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex);
        apiError.setMessage(String.format("The parameter '%s' of value '%s' could not be converted to type '%s'",
                ex.getName(), ex.getValue(), requiredType.getName()));
        return handleExceptionInternal(ex, apiError, HTTP_HEADERS, apiError.getStatus(), request);
    }

    @ExceptionHandler(value = { IllegalArgumentException.class, IllegalStateException.class })
    protected ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {
        log.error("Unable to parse data {}", ex);
        final ApiError apiError = new ApiError(HttpStatus.CONFLICT, ex);
        apiError.setMessage("This should be application specific");
        return handleExceptionInternal(ex, apiError, HTTP_HEADERS, apiError.getStatus(), request);
    }

    @ExceptionHandler(value = SecurityJwtException.class)
    protected ResponseEntity<Object> handleConflict(SecurityJwtException ex, WebRequest request) {
        final ApiError apiError = new ApiError(ex.getHttpStatus(), ex);
        apiError.setMessage(ex.getMessage());
        return handleExceptionInternal(ex, apiError, HTTP_HEADERS, apiError.getStatus(), request);
    }

    @ExceptionHandler(value = org.springframework.security.access.AccessDeniedException.class)
    protected ResponseEntity<Object> handleConflict(org.springframework.security.access.AccessDeniedException ex,
            WebRequest request) {
        log.error("AccessDenied", ex);
        final ApiError apiError = new ApiError(HttpStatus.FORBIDDEN, ex);
        // apiError.setMessage(ex.getMessage());
        return handleExceptionInternal(ex, apiError, HTTP_HEADERS, apiError.getStatus(), request);
    }

    @ExceptionHandler(value = HttpClientErrorException.class)
    protected ResponseEntity<Object> handleConflict(HttpClientErrorException ex, WebRequest request) {
        final ApiError apiError = new ApiError(HttpStatus.FORBIDDEN, ex);
        apiError.setMessage(ex.getMessage());
        return handleExceptionInternal(ex, apiError, HTTP_HEADERS, apiError.getStatus(), request);
    }

    @ExceptionHandler(value = TimeExpiredException.class)
    protected ResponseEntity<Object> handleConflict(TimeExpiredException ex, WebRequest request) {
        log.error("Expired time for this operation", ex);
        final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex);
        return handleExceptionInternal(ex, apiError, HTTP_HEADERS, apiError.getStatus(), request);
    }

    @ExceptionHandler(value = MaxLevelCommentException.class)
    protected ResponseEntity<Object> handleConflict(MaxLevelCommentException ex, WebRequest request) {
        log.error("Reached max comment level", ex);
        final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex);
        return handleExceptionInternal(ex, apiError, HTTP_HEADERS, apiError.getStatus(), request);
    }

    @ExceptionHandler(value = AlreadyVotedException.class)
    protected ResponseEntity<Object> handleConflict(AlreadyVotedException ex, WebRequest request) {
        log.error("User already voted", ex);
        final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex);
        return handleExceptionInternal(ex, apiError, HTTP_HEADERS, apiError.getStatus(), request);
    }

    @ExceptionHandler(value = ForbiddenException.class)
    protected ResponseEntity<Object> handleConflict(ForbiddenException ex, WebRequest request) {
        log.error("Voting is forbidden", ex);
        final ApiError apiError = new ApiError(HttpStatus.FORBIDDEN, ex);
        return handleExceptionInternal(ex, apiError, HTTP_HEADERS, apiError.getStatus(), request);
    }

    @ExceptionHandler(value = EmailSendException.class)
    protected ResponseEntity<Object> handleConflict(EmailSendException ex, WebRequest request) {
        final ApiError apiError = new ApiError(ex.getHttpStatus(), ex);
        apiError.setMessage(ex.getMessage());
        return handleExceptionInternal(ex, apiError, HTTP_HEADERS, apiError.getStatus(), request);
    }

    @ExceptionHandler(value = UserValidationException.class)
    protected ResponseEntity<Object> handleConflict(UserValidationException ex, WebRequest request) {
        final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex);
        apiError.setMessage(ex.getMessage());
        return handleExceptionInternal(ex, apiError, HTTP_HEADERS, apiError.getStatus(), request);
    }
}
