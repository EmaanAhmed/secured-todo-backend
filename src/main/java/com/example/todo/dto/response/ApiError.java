package com.example.todo.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record ApiError(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        String path,
        List<FieldValidationError> fieldErrors) {
    public ApiError {
        if (fieldErrors == null) {
            fieldErrors = List.of();
        }
    }

    public record FieldValidationError(
            String field,
            String message) {
    }
}
