package com.systemproject.taskmanagement.services;

import com.systemproject.taskmanagement.dto.TaskDto;
import com.systemproject.taskmanagement.dto.request.TaskEditRequestDto;
import com.systemproject.taskmanagement.dto.response.*;
import com.systemproject.taskmanagement.entities.Task;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TaskService {
    void createTask(TaskDto task, String authorId);
    Task taskMapping(TaskDto task, String authorId);
    TaskEditResponseDto editTaskByTitle(String taskTitle, TaskEditRequestDto task, String email);
    void deleteTask(String taskId, String email);
    TaskDto getTaskById(String id);
    List<TaskDto> getAllTasksCreatedByUser(String email);
    List<TaskDto> getAllTasksAssignedToUser(String email);
    List<TaskDto> getAllUserTasks(Integer pageNum, Integer pageSize, String email);
}
