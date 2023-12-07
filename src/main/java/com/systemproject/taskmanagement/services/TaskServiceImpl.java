package com.systemproject.taskmanagement.services;

import com.systemproject.taskmanagement.entities.Task;
import com.systemproject.taskmanagement.pojo.TaskPriority;
import com.systemproject.taskmanagement.pojo.TaskStatus;
import com.systemproject.taskmanagement.repository.TaskRepository;
import com.systemproject.taskmanagement.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

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
    public Task createTask(Task task, Long creatorId, String performerEmail, TaskStatus status, TaskPriority priority) {
        task.setAuthor(unwrapUser(userRepository.findById(creatorId)));
        task.setPerformer(unwrapUser(userRepository.findUserByEmail(performerEmail)));
        task.setTaskStatus(status);
        task.setTaskPriority(priority);
        return taskRepository.save(task);
    }

    //TODO: create custom exception
    @Override
    public Task editTask(Task task) {
        return taskRepository.findById(task.getId()).map(updatedTask -> {
            updatedTask.setTitle(task.getTitle());
            updatedTask.setDescription(task.getDescription());
            updatedTask.setComment(task.getComment());
            updatedTask.setTaskStatus(task.getTaskStatus());
            return taskRepository.save(updatedTask);
        }).orElseThrow(() -> new NoSuchElementException());
    }

    //TODO: create custom exception for non-existing tasks
    @Override
    public void deleteTask(Long taskId) {
        taskRepository.deleteById(taskId);
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
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
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
