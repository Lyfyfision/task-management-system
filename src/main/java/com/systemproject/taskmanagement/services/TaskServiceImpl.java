package com.systemproject.taskmanagement.services;

import com.systemproject.taskmanagement.dto.TaskDto;
import com.systemproject.taskmanagement.entities.Task;
import com.systemproject.taskmanagement.entities.User;
import com.systemproject.taskmanagement.pojo.TaskPriority;
import com.systemproject.taskmanagement.pojo.TaskStatus;
import com.systemproject.taskmanagement.repository.TaskRepository;
import com.systemproject.taskmanagement.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static com.systemproject.taskmanagement.services.UserServiceImpl.unwrapUser;

@Service
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {
    private TaskRepository taskRepository;
    private UserRepository userRepository;
    @Override
    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    @Override
    public Task taskMapping(TaskDto task, Long authorId) {
        var performerEmail = task.getPerformerEmail();
        User performer = userRepository.findByEmail(performerEmail)
                .orElseThrow(() -> new IllegalArgumentException("User with email:" + performerEmail + "could not be found"));
        Task returnedTask = new Task();
        returnedTask.setTitle(task.getTitle());
        returnedTask.setDescription(task.getDescription());
        returnedTask.setTaskStatus(task.getStatus());
        returnedTask.setTaskPriority(task.getPriority());
        returnedTask.setComment(task.getComment());
        returnedTask.setAuthor(unwrapUser(userRepository.findById(authorId)));
        returnedTask.setPerformer(unwrapUser(userRepository.findByEmail(performer.getEmail())));
        return returnedTask;
    }

    //TODO: create custom exception

    @Override
    public Task editTask(Task task) {
        return taskRepository.findById(task.getId()).map(updatedTask -> {
            updatedTask.setTitle(task.getTitle());
            updatedTask.setDescription(task.getDescription());
            updatedTask.setComment(task.getComment());
            updatedTask.setTaskStatus(task.getTaskStatus());
            updatedTask.setTaskPriority(task.getTaskPriority());
            return taskRepository.save(updatedTask);
        }).orElseThrow(() -> new NoSuchElementException("Task with provided id - " + task.getId() + " doesn't exist"));
    }

    //TODO: create custom exception for non-existing tasks
    @Override
    public void deleteTask(Long taskId, String email) {
        if (taskRepository.findById(taskId).get().getAuthor().getEmail().equals(email)) {
            taskRepository.deleteById(taskId);
        } else throw new AccessDeniedException("Only author's should delete tasks");
    }

    @Override
    public Task getTaskById(Long id) {
        return unwrapTask(taskRepository.findById(id));
    }

    @Override
    public List<Task> getAllTasksCreatedByUser(String email) {
        return taskRepository.findTasksByAuthorEmail(email);
    }

    @Override
    public List<Task> getAllTasksAssignedToUser(String email) {
        return taskRepository.findTasksByPerformerEmail(email);
    }

    @Override
    public List<Task> getAllTasks(Integer pageNum, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNum, pageSize, Sort.by(sortBy));

        Page<Task> pagedResult = taskRepository.findAll(paging);

        if(pagedResult.hasContent()) {
            return pagedResult.getContent();
        } else {
            return new ArrayList<Task>();
        }
    }

    @Override
    public List<Task> getAllTasksByStatus(TaskStatus status) {
        return taskRepository.findTasksByTaskStatus(status);
    }

    @Override
    public List<Task> getAllTasksByPriority(TaskPriority priority) {
        return taskRepository.findTasksByTaskPriority(priority);
    }

    static Task unwrapTask(Optional<Task> entity) {
        if (entity.isPresent()) return entity.get();
        else throw new NoSuchElementException();
    }
}
