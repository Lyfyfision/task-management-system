package com.systemproject.taskmanagement.services;

import com.systemproject.taskmanagement.entities.Task;
import com.systemproject.taskmanagement.pojo.TaskPriority;
import com.systemproject.taskmanagement.pojo.TaskStatus;

import java.util.List;

public interface TaskService {
    Task createTask(Task task, Long creatorId, String performerEmail, TaskStatus status, TaskPriority priority);
    Task editTask(Task task);
    void deleteTask(Long taskId, String email);
    Task getTaskById(Long id);
    List<Task> getAllTasksCreatedByUser(String email);
    List<Task> getAllTasksAssignedToUser(String email);
    List<Task> getAllTasks();
    List<Task> getAllTasksByStatus(TaskStatus status);
    List<Task> getAllTasksByPriority(TaskPriority priority);
}
