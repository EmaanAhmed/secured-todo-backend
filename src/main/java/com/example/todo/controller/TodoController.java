package com.example.todo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.todo.dto.request.TodoCreateRequest;
import com.example.todo.dto.request.TodoUpdateRequest;
import com.example.todo.dto.response.TodoResponse;
import com.example.todo.security.AuthenticatedUser;
import com.example.todo.service.TodoService;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/todos")
public class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @PostMapping
    public ResponseEntity<TodoResponse> createTodo(
            @AuthenticationPrincipal AuthenticatedUser currentUser,
            @Valid @RequestBody TodoCreateRequest request) {
        TodoResponse response = todoService.createTodo(currentUser.getId(), request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<Page<TodoResponse>> getTodos(@AuthenticationPrincipal AuthenticatedUser currentUser,
            @RequestParam(required = false) Boolean completed, @RequestParam(required = false) String search,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<TodoResponse> response = todoService.getTodos(currentUser.getId(), completed, search, pageable);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{todoId}")
    public ResponseEntity<TodoResponse> getTodo(@AuthenticationPrincipal AuthenticatedUser currentUser,
            @PathVariable Long todoId) {
        TodoResponse response = todoService.getTodo(currentUser.getId(), todoId);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{todoId}")
    public ResponseEntity<TodoResponse> updateTodo(@AuthenticationPrincipal AuthenticatedUser currentUser,
            @PathVariable Long todoId,
            @Valid @RequestBody TodoUpdateRequest request) {
        TodoResponse response = todoService.updateTodo(currentUser.getId(), todoId, request);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{todoId}")
    public ResponseEntity<Void> deleteTodo(@AuthenticationPrincipal AuthenticatedUser currentUser,
            @PathVariable Long todoId) {
        todoService.deleteTodo(currentUser.getId(), todoId);
        return ResponseEntity.noContent().build();
    }
}
