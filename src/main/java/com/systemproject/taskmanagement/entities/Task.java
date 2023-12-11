package com.systemproject.taskmanagement.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.systemproject.taskmanagement.pojo.TaskPriority;
import com.systemproject.taskmanagement.pojo.TaskStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.security.core.context.SecurityContextHolder;

@Entity
@Table(name = "tasks")
@Getter
@Setter
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank
    @NonNull
    @Column(nullable = false)
    private String title;

    @NotBlank
    @NonNull
    @Column(nullable = false)
    private String description;

    @Column(name = "task_status",nullable = false)
    @Enumerated(EnumType.STRING)
    private TaskStatus taskStatus;

    @Column(name = "task_priority", nullable = false)
    @Enumerated(EnumType.STRING)
    private TaskPriority taskPriority;

    @Column
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", referencedColumnName = "id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "password"})
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "performer_id", referencedColumnName = "id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "password"})
    private User performer;

    public Task() {

    }

    public Task(@NotBlank @NonNull String title, @NotBlank @NonNull String description, User performer) {
        this.title = title;
        this.description = description;
        this.performer = performer;
    }

    public Task(@NotBlank @NonNull String title, @NotBlank @NonNull String description, User performer, TaskPriority taskPriority) {
        this.title = title;
        this.description = description;
        this.performer = performer;
        this.taskPriority = taskPriority;
    }

    @PrePersist
    public void setUp() {
        this.taskPriority = TaskPriority.DEFAULT_PRIORITY;
        this.taskStatus = TaskStatus.PENDING;
    }

}
