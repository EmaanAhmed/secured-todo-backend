package com.example.todo.dto.response;

import java.time.LocalDateTime;

import com.example.todo.entity.Todo;

public record TodoResponse(
        Long id,
        String title,
        String description,
        boolean completed,
        LocalDateTime dueAt,
        boolean reminderSent,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {

    public static TodoResponse fromEntity(Todo todo) {
        return new TodoResponse(todo.getId(), todo.getTitle(), todo.getDescription(), todo.isCompleted(),
                todo.getDueAt(), todo.isReminderSent(), todo.getCreatedAt(), todo.getUpdatedAt());
    }
}
