package com.systemproject.taskmanagement.repository;

import com.systemproject.taskmanagement.entities.Task;
import com.systemproject.taskmanagement.constant.TaskPriority;
import com.systemproject.taskmanagement.constant.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {
    List<Task> findTasksByAuthorId(UUID id);
    List<Task> findTasksByPerformerId(UUID Id);
    List<Task> findTasksByTaskStatus(TaskStatus taskStatus);
    List<Task> findTasksByTaskPriority(TaskPriority taskPriority);
    List<Task> findTasksByAuthorEmail(String email);
    List<Task> findTasksByPerformerEmail(String email);
    @Override
    Page<Task> findAll(Pageable pageable);
    Page<Task> findTasksByAuthorEmailAndPerformer_Email(String firstEmail, String email, Pageable pageable);

    @Query("SELECT t FROM Task t WHERE t.author.email = :email OR t.performer.email = :email")
    Page<Task> findAllTasksByUserEmail(@Param("email") String email, Pageable pageable);
}
