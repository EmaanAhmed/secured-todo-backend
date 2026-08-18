package com.example.todo.dto.response;

import com.example.todo.entity.User;

import java.time.LocalDateTime;

public record UserResponse(
        Long id,
        String email,
        String fullName,
        LocalDateTime createdAt) {

    public static UserResponse fromEntity(User user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getFullName(),
                user.getCreatedAt());
    }

}