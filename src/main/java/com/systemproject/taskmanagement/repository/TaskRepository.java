package com.systemproject.taskmanagement.repository;

import com.systemproject.taskmanagement.entities.Task;
import com.systemproject.taskmanagement.entities.User;
import com.systemproject.taskmanagement.pojo.TaskPriority;
import com.systemproject.taskmanagement.pojo.TaskStatus;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findTasksByAuthorId(Long id);
    List<Task> findTasksByPerformerId(Long Id);
    List<Task> findTasksByTaskStatus(TaskStatus taskStatus);
    List<Task> findTasksByTaskPriority(TaskPriority taskPriority);
    List<Task> findTasksByAuthorEmail(String email);
    List<Task> findTasksByPerformerEmail(String email);
}