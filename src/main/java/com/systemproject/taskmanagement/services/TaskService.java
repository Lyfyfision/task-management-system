package com.systemproject.taskmanagement.services;

import com.systemproject.taskmanagement.dto.TaskDto;
import com.systemproject.taskmanagement.entities.Task;
import com.systemproject.taskmanagement.pojo.TaskPriority;
import com.systemproject.taskmanagement.pojo.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TaskService {
    Task createTask(Task task);
    Task taskMapping(TaskDto task, Long id);
    Task editTask(Task task);
    void deleteTask(Long taskId, String email);
    Task getTaskById(Long id);
    List<Task> getAllTasksCreatedByUser(String email);
    List<Task> getAllTasksAssignedToUser(String email);
    List<Task> getAllTasks(Integer pageNum, Integer pageSize, String sortBy);
    List<Task> getAllTasksByStatus(TaskStatus status);
    List<Task> getAllTasksByPriority(TaskPriority priority);
}
