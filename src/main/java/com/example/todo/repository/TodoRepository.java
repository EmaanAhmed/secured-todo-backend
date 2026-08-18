package com.example.todo.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.todo.entity.Todo;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long>, JpaSpecificationExecutor<Todo> {

  Optional<Todo> findByIdAndUserId(Long id, Long userId);

  boolean existsByIdAndUserId(Long id, Long userId);

  List<Todo> findAllByUserIdOrderByCreatedAtDesc(Long userId);

  List<Todo> findByUserIdAndCompleted(Long userId, boolean completed);

  Page<Todo> findAllByUserId(Long userId, Pageable pageable);

  @Query("""
      SELECT t
      FROM Todo t
      JOIN FETCH t.user
      WHERE t.reminderSent = false
        AND t.dueAt IS NOT NULL
        AND t.dueAt <= :threshold
      """)
  List<Todo> findDueTodosForReminder(@Param("threshold") LocalDateTime threshold);

  @Modifying(clearAutomatically = true)
  @Query("""
      UPDATE Todo t
      SET t.reminderSent = true
      WHERE t.id = :id
      """)
  int markReminderSent(@Param("id") Long id);
}
