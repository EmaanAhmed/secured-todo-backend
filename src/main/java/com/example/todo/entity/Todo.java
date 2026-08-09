package com.example.todo.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "todos")
public class Todo extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "title", nullable = false, columnDefinition = "VARCHAR(255)")
    private String title;

    @Column(name = "description", columnDefinition = "VARCHAR(1000)")
    private String description;

    @Column(name = "completed", nullable = false, columnDefinition = "TINYINT(1)")
    private boolean completed = false;

    @Column(name = "due_at", columnDefinition = "DATETIME(6)")
    private LocalDateTime dueAt;

    @Column(name = "reminder_sent", nullable = false, columnDefinition = "TINYINT(1)")
    private boolean reminderSent = false;

    protected Todo() {
        // JPA requires a no-argument constructor
    }

    public Todo(User user, String title, String description, LocalDateTime dueAt) {
        this.user = user;
        this.title = title;
        this.description = description;
        this.dueAt = dueAt;
        this.completed = false;
        this.reminderSent = false;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public LocalDateTime getDueAt() {
        return dueAt;
    }

    public void setDueAt(LocalDateTime dueAt) {
        this.dueAt = dueAt;
    }

    public boolean isReminderSent() {
        return reminderSent;
    }

    public void setReminderSent(boolean reminderSent) {
        this.reminderSent = reminderSent;
    }

    @Override
    public String toString() {
        return "Todo{" + "id=" + getId() + ", title='" + title + '\'' + ", description='" + description + '\''
                + ", completed=" + completed + ", dueAt=" + dueAt + ", reminderSent=" + reminderSent + '}';
    }
}
