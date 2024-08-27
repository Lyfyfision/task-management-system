package com.systemproject.taskmanagement.services;

import com.systemproject.taskmanagement.dto.TaskDto;
import com.systemproject.taskmanagement.dto.mapper.TaskMapper;
import com.systemproject.taskmanagement.dto.request.TaskEditRequestDto;
import com.systemproject.taskmanagement.dto.response.*;
import com.systemproject.taskmanagement.entities.Task;
import com.systemproject.taskmanagement.entities.User;
import com.systemproject.taskmanagement.constant.TaskPriority;
import com.systemproject.taskmanagement.constant.TaskStatus;
import com.systemproject.taskmanagement.exceptions.TaskNotFoundException;
import com.systemproject.taskmanagement.repository.TaskRepository;
import com.systemproject.taskmanagement.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.systemproject.taskmanagement.services.UserServiceImpl.unwrapUser;

@Service
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {
    private TaskRepository taskRepository;
    private UserRepository userRepository;
    private TaskMapper taskMapper;

    @Override
    public void createTask(TaskDto task, String authorEmail) {
        taskRepository.save(taskMapping(task, authorEmail));
    }

    @Override
    public Task taskMapping(TaskDto task, String authorEmail) {
        var performerEmail = task.getPerformerEmail();
        User performer = userRepository.findByEmail(task.getPerformerEmail())
                .orElseThrow(() -> new IllegalArgumentException("User with email:" + performerEmail + "could not be found"));
        User author = userRepository.findByEmail(authorEmail)
                .orElseThrow(() -> new IllegalArgumentException("User with email:" + performerEmail + "could not be found"));
        return Task.builder()
                .title(task.getTitle())
                .description(task.getDescription())
                .taskStatus(task.getStatus())
                .taskPriority(task.getPriority())
                .comment(task.getComment())
                .author(author)
                .performer(performer)
                .build();
    }

    @Override
    @Transactional
    public TaskEditResponseDto editTask(String taskId, TaskEditRequestDto updatedTask) {
        Optional<Task> optionalTask = taskRepository.findById(UUID.fromString(taskId));
        if(optionalTask.isPresent()) {
            Task existingTask = optionalTask.get();
            existingTask.setTitle(updatedTask.getNewTitle());
            existingTask.setDescription(updatedTask.getNewDescription());
            existingTask.setCreationDate(updatedTask.getNewDate());
            taskRepository.save(existingTask);
            return new TaskEditResponseDto("Таска успешно отредактирована");
        } else {
            throw new TaskNotFoundException("Task not found with id: " + taskId);
        }
    }

    @Override
    public void deleteTask(String taskId, String email) {
        if (taskRepository.findById(UUID.fromString(taskId)).get().getAuthor().getEmail().equals(email)) {
            taskRepository.deleteById(UUID.fromString(taskId));
        } else throw new AccessDeniedException("Only author's should delete their tasks");
    }

    @Override
    public TaskDto getTaskById(String id) {
        return taskMapper.toDto(unwrapTask(taskRepository.findById(UUID.fromString(id))));
    }

    @Override
    public List<TaskDto> getAllTasksCreatedByUser(String email) {
        List<Task> list = taskRepository.findTasksByAuthorEmail(email);
        if (list.isEmpty()) throw new TaskNotFoundException("Тасок у этого автора не найдено");
        return list.stream()
                .map(taskMapper::toDto)
                .toList();
    }

    @Override
    public List<TaskDto> getAllTasksAssignedToUser(String email) {
        List<Task> list = taskRepository.findTasksByAuthorEmail(email);
        if (list.isEmpty()) throw new TaskNotFoundException("Тасок у этого исполнителя не найдено");
        return list.stream()
                .map(taskMapper::toDto)
                .toList();
    }

    @Override
    public List<TaskDto> getAllUserTasks(Integer pageNum, Integer pageSize, String email) {
        Pageable paging = PageRequest.of(pageNum, pageSize);
        Page<Task> pagedResult = taskRepository.findAllTasksByUserEmail(email, paging);
        return pagedResult.getContent().stream()
                .map(taskMapper::toDto)
                .toList();
    }



    static Task unwrapTask(Optional<Task> entity) {
        if (entity.isPresent()) return entity.get();
        else throw new NoSuchElementException();
    }
}
