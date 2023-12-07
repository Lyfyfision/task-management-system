package com.systemproject.taskmanagement.entities;

import com.systemproject.taskmanagement.pojo.TaskPriority;
import com.systemproject.taskmanagement.pojo.TaskStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "tasks")
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
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

    @NonNull
    @Column(name = "task_status",nullable = false)
    @Enumerated(EnumType.STRING)
    private TaskStatus taskStatus;

    @NonNull
    @Column(name = "task_priority", nullable = false)
    @Enumerated(EnumType.STRING)
    private TaskPriority taskPriority;

    @Column
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @NonNull
    @JoinColumn(name = "author_id", referencedColumnName = "id", nullable = false)
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @NonNull
    @JoinColumn(name = "performer_id", referencedColumnName = "id", nullable = false)
    private User performer;

    @PrePersist
    public void setUp() {
        this.taskPriority = TaskPriority.DEFAULT_PRIORITY;
        this.taskStatus = TaskStatus.PENDING;
    }

}
