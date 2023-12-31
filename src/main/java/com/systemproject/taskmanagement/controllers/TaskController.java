package com.systemproject.taskmanagement.controllers;

import com.systemproject.taskmanagement.dto.TaskDto;
import com.systemproject.taskmanagement.entities.Task;
import com.systemproject.taskmanagement.entities.User;
import com.systemproject.taskmanagement.services.TaskServiceImpl;
import com.systemproject.taskmanagement.services.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
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
    @Operation(summary = "Create a task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Task was successfully created",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Task.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid task request", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized user can't create tasks", content = @Content)
    })
    public ResponseEntity<Task> createTask(@ParameterObject @RequestBody TaskDto task, Principal principal) {
        User currentUser = userService.getUser(principal.getName());
        Task savedTask = taskService.taskMapping(task, currentUser.getId());
        taskService.createTask(savedTask);
        return new ResponseEntity<>(savedTask, HttpStatus.CREATED);
    }
    @PutMapping("/{email}/edit/{task_id}")
    @PreAuthorize("#email == principal.username")
    @Operation(summary = "Edit logged in user's task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task was changed",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Task.class))}),
            @ApiResponse(responseCode = "400", description = "Task with such id doesn't exist", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized user can't edit tasks", content = @Content)
    })
    public ResponseEntity<Task> editTask(@PathVariable("task_id") Long id, @PathVariable("email") String email) {
        taskService.editTask(taskService.getTaskById(id));
        return new ResponseEntity<>(taskService.getTaskById(id), HttpStatus.OK);
    }

    //TODO: add author-check on delete
    @DeleteMapping("/{email}/delete/{task_id}")
    @PreAuthorize("#email == principal.username")
    @Operation(summary = "Delete Task by provided task_id (may be acquired only by this task's creator)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task was deleted",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Task with such id doesn't exist", content = @Content),
            @ApiResponse(responseCode = "401", description =
                    "Unauthorized user can't delete tasks / Logged user cannot delete another user's tasks", content = @Content)
    })
    public ResponseEntity<?> deleteTask(@Parameter @PathVariable("task_id") @RequestParam Long id, @PathVariable("email") String email) {
        taskService.deleteTask(id, email);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping("/get-all")
    @Operation(summary = "Get all existing Tasks (Pagination and sorting params may be including in request by User)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get all Tasks including pagination and sorting",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Task.class))}),
            @ApiResponse(responseCode = "401", description =
                    "Unauthorized user can't delete tasks", content = @Content)
    })
    public List<Task> getAllTasks(@Parameter @RequestParam(defaultValue = "0") Integer pageNum,
                                  @Parameter @RequestParam(defaultValue = "10") Integer pageSize,
                                  @Parameter @RequestParam(defaultValue = "id") String sortBy) {
        return taskService.getAllTasks(pageNum, pageSize, sortBy);
    }
    @GetMapping("/get-created/{author_email}")
    @Operation(summary = "Get all existing Tasks created by specific User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get all tasks by specific author",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Task.class))}),
            @ApiResponse(responseCode = "400", description = "Author with such id doesn't exist", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized user can't get tasks", content = @Content)
    })
    public List<Task> getAllCreatedTasksByAuthorEmail(@Parameter @PathVariable("author_email") @RequestParam String email) {
        return taskService.getAllTasksCreatedByUser(email);
    }
    @GetMapping("/get-assigned/{performer_email}")
    @Operation(summary = "Get all existing Tasks performed by specific User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get all tasks by specific performer",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Task.class))}),
            @ApiResponse(responseCode = "400", description = "Performer with such id doesn't exist", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized user can't get tasks", content = @Content)
    })
    public List<Task> getAllAssignedTasksByPerformerEmail(@Parameter @PathVariable("performer_email") @RequestParam String email) {
        return taskService.getAllTasksAssignedToUser(email);
    }
}
