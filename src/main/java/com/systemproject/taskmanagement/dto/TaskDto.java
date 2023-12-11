package com.systemproject.taskmanagement.dto;

import com.systemproject.taskmanagement.pojo.TaskPriority;
import com.systemproject.taskmanagement.pojo.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

/**
 * A DTO for the {@link com.systemproject.taskmanagement.entities.Task} entity
 */
@Data
public class TaskDto implements Serializable {
    @NotBlank
    private final String title;
    @NotBlank
    private final String description;
    private TaskStatus status;
    private TaskPriority priority;
    private final String comment;
    @NotBlank
    private final Long authorId;
    @NotBlank
    private final String performerEmail;
}