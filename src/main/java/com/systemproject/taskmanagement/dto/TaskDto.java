package com.systemproject.taskmanagement.dto;

import com.systemproject.taskmanagement.constant.TaskPriority;
import com.systemproject.taskmanagement.constant.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * A DTO for the {@link com.systemproject.taskmanagement.entities.Task} entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskDto implements Serializable {
    @NotBlank
    private String title;
    @NotBlank
    private String description;
    @NotBlank
    private LocalDate creationDate;
    private TaskStatus status;
    private TaskPriority priority;
    private String comment;
    @NotBlank
    private String performerEmail;
}