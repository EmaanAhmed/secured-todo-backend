package com.example.todo.dto.request;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TodoUpdateRequest(
        @NotBlank(message = "Title is required") @Size(max = 255, message = "Title must be at most 255 characters") String title,

        @Size(max = 1000, message = "Description must be at most 1000 characters") String description,

        @NotNull(message = "Completed status is required") Boolean completed,

        LocalDateTime dueAt) {
}
