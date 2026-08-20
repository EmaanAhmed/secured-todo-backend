package com.example.todo.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.todo.dto.request.RegisterRequest;
import com.example.todo.dto.response.UserResponse;
import com.example.todo.entity.User;
import com.example.todo.exception.DuplicateResourceException;
import com.example.todo.exception.ResourceNotFoundException;
import com.example.todo.repository.UserRepository;

@Service
@Transactional(readOnly = true)
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserResponse register(RegisterRequest request) {
        String email = request.email().trim().toLowerCase();
        String fullName = request.fullName().trim();

        if (userRepository.existsByEmailIgnoreCase(email)) {
            throw new DuplicateResourceException("Email already exists: " + email);
        }

        User user = new User(email, passwordEncoder.encode(request.password()), fullName);

        User savedUser = userRepository.save(user);

        return UserResponse.fromEntity(savedUser);

    }

    public UserResponse getCurrentUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        return UserResponse.fromEntity(user);
    }

}
