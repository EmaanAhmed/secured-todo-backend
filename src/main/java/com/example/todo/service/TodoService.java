package com.example.todo.service;

import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.todo.dto.request.TodoCreateRequest;
import com.example.todo.dto.request.TodoUpdateRequest;
import com.example.todo.dto.response.TodoResponse;
import com.example.todo.entity.Todo;
import com.example.todo.entity.User;
import com.example.todo.exception.ResourceNotFoundException;
import com.example.todo.repository.TodoRepository;
import com.example.todo.repository.UserRepository;
import com.example.todo.repository.specification.TodoSpecifications;

@Service
@Transactional(readOnly = true)
public class TodoService {
    private final TodoRepository todoRepository;
    private final UserRepository userRepository;

    public TodoService(TodoRepository todoRepository, UserRepository userRepository) {
        this.todoRepository = todoRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public TodoResponse createTodo(Long userId, TodoCreateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Todo todo = new Todo(user, request.title(), request.description(), request.dueAt());

        Todo savedTodo = todoRepository.save(todo);

        return TodoResponse.fromEntity(savedTodo);
    }

    public TodoResponse getTodo(Long userId, Long todoId) {
        Todo todo = findTodo(userId, todoId);

        return TodoResponse.fromEntity(todo);
    }

    public Page<TodoResponse> getTodos(Long userId, Boolean completed, String search, Pageable pageable) {
        Specification<Todo> specification = Specification.where(TodoSpecifications.belongsToUser(userId))
                .and(TodoSpecifications.isCompleted(completed))
                .and(TodoSpecifications.titleOrDerscriptionContains(search));

        return todoRepository.findAll(specification, pageable).map(TodoResponse::fromEntity);
    }

    @Transactional
    public TodoResponse updateTodo(Long userId, Long todoId, TodoUpdateRequest request) {
        Todo todo = findTodo(userId, todoId);

        boolean dueAtChanged = !Objects.equals(todo.getDueAt(), request.dueAt());

        todo.setTitle(request.title());
        todo.setDescription(request.description());
        todo.setCompleted(Boolean.TRUE.equals(request.completed()));
        todo.setDueAt(request.dueAt());

        if (dueAtChanged) {
            todo.setReminderSent(false);
        }

        Todo updatedTodo = todoRepository.save(todo);

        return TodoResponse.fromEntity(updatedTodo);
    }

    @Transactional
    public void deleteTodo(Long userId, Long todoId) {
        Todo todo = findTodo(userId, todoId);

        todoRepository.delete(todo);
    }

    private Todo findTodo(Long userId, Long todoId) {
        return todoRepository.findByIdAndUserId(todoId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Todo not found with id: " + todoId));
    }
}
