package com.systemproject.taskmanagement.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.systemproject.taskmanagement.constant.TaskPriority;
import com.systemproject.taskmanagement.constant.TaskStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import lombok.*;
import org.springframework.security.core.Authentication;

import java.security.Principal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "tasks")
@Getter
@Setter
@Builder
@AllArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @NotBlank
    @NonNull
    @Column(nullable = false)
    private String title;

    @NotBlank
    @NonNull
    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private LocalDate creationDate;

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

    public Task(@NotBlank @NonNull String title, @NotBlank @NonNull String description, User performer, User author) {
        this.title = title;
        this.description = description;
        this.performer = performer;
        this.author = author;
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
        this.creationDate = LocalDate.now();
    }
}
