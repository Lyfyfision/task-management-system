package com.systemproject.taskmanagement.controllers;

import com.systemproject.taskmanagement.entities.Task;
import com.systemproject.taskmanagement.services.TaskServiceImpl;
import com.systemproject.taskmanagement.services.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/tasks")
@AllArgsConstructor
public class TaskController {
    private final TaskServiceImpl taskService;
    private final UserServiceImpl userService;
    @PostMapping("/create")
    public ResponseEntity<?> createTask(@Valid @RequestBody Task task, Principal principal) {
        taskService.createTask(task, userService.getUser(principal.getName()).getId(), task.getPerformer().getEmail(),
                task.getTaskStatus(), task.getTaskPriority());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @PutMapping("/{email}/edit/{task_id}")
    @PreAuthorize("#email == principal.username")
    public ResponseEntity<?> editTask(@PathVariable("task_id") Long id, @PathVariable("email") String email) {
        taskService.editTask(taskService.getTaskById(id));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //TODO: add author-check on delete
    @DeleteMapping("/{email}/delete/{task_id}")
    @PreAuthorize("#email == principal.username")
    public ResponseEntity<?> deleteTask(@PathVariable("task_id") Long id, @PathVariable("email") String email) {
        taskService.deleteTask(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping("/get-all")
    public List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }
    @GetMapping("/get-created/{author_email}")
    public List<Task> getAllCreatedTasksByAuthorEmail(@PathVariable("author_email") @RequestParam String email) {
        return taskService.getAllTasksCreatedByUser(email);
    }
    @GetMapping("/get-assigned/{performer_email}")
    public List<Task> getAllAssignedTasksByPerformerEmail(@PathVariable("performer_email") @RequestParam String email) {
        return taskService.getAllTasksAssignedToUser(email);
    }
}
