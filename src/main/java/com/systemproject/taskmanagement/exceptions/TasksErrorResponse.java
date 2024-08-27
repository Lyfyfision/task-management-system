package com.systemproject.taskmanagement.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TasksErrorResponse {
    private String message;
}
