package com.example.todo.repository.specification;

import java.time.LocalDateTime;

import org.springframework.data.jpa.domain.Specification;

import com.example.todo.entity.Todo;

import jakarta.persistence.criteria.Predicate;

public final class TodoSpecifications {
    private TodoSpecifications() {

    }

    public static Specification<Todo> belongsToUser(Long userId) {
        return (root, query, criteriaBuilder) -> {
            if (userId == null) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.equal(root.get("user").get("id"), userId);
        };
    }

    public static Specification<Todo> isCompleted(Boolean completed) {
        return (root, query, criteriaBuilder) -> {
            if (completed == null) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.equal(root.get("completed"), completed);

        };
    }

    public static Specification<Todo> titleOrDerscriptionContains(String search) {
        return (root, query, criteriaBuilder) -> {
            if (search == null || search.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            String pattern = "%" + search.trim().toLowerCase() + "%";

            Predicate titlePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), pattern);

            Predicate descriptionPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("description")),
                    pattern);

            return criteriaBuilder.or(titlePredicate, descriptionPredicate);
        };
    }

    public static Specification<Todo> dueBefore(LocalDateTime dueBefore) {
        return (root, query, criteriaBuilder) -> {
            if (dueBefore == null) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.lessThanOrEqualTo(root.get("dueAt"), dueBefore);
        };
    }
}
