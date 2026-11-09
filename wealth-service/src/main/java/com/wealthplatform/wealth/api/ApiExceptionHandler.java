package com.wealthplatform.wealth.api;

import com.wealthplatform.wealth.service.WealthException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ApiExceptionHandler {
    private static ResponseEntity<ApiError> error(HttpStatus status, String message, Map<String, String> fieldErrors) {
        return ResponseEntity.status(status).body(new ApiError(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                fieldErrors
        ));
    }

    @ExceptionHandler(WealthException.class)
    public ResponseEntity<ApiError> handleWealthException(WealthException exception) {
        return error(exception.getStatus(), exception.getMessage(), Map.of());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException exception) {
        Map<String, String> fieldErrors = exception.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        fieldError -> fieldError.getDefaultMessage() == null ? "Invalid value" : fieldError.getDefaultMessage(),
                        (first, second) -> first
                ));
        return error(HttpStatus.BAD_REQUEST, "Request validation failed", fieldErrors);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleConstraintViolation(DataIntegrityViolationException exception) {
        return error(HttpStatus.CONFLICT, "The request conflicts with existing data", Map.of());
    }

    public record ApiError(
            Instant timestamp,
            int status,
            String error,
            String message,
            Map<String, String> fieldErrors
    ) {
    }
}
