package com.systemproject.taskmanagement.controllers;

import com.systemproject.taskmanagement.dto.TaskDto;
import com.systemproject.taskmanagement.dto.request.TaskEditRequestDto;
import com.systemproject.taskmanagement.dto.response.NewTaskResponseDto;
import com.systemproject.taskmanagement.dto.response.TaskEditResponseDto;
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
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("api/tasks")
@AllArgsConstructor
public class TaskController {

    private final TaskServiceImpl taskService;
    private final UserServiceImpl userService;

    @PostMapping()
    @Operation(summary = "Create a task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Task was successfully created",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Task.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid task request", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized user can't create tasks", content = @Content)
    })
    public ResponseEntity<NewTaskResponseDto> createTask(@ParameterObject @RequestBody TaskDto task, Principal principal) {
        User currentUser = userService.getUserByEmail(principal.getName());
        taskService.createTask(task, currentUser.getEmail());
        NewTaskResponseDto response = new NewTaskResponseDto(task.getTitle());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PatchMapping("/{email}/{task_title}")
    @PreAuthorize("#email == principal.username")
    @Operation(summary = "Edit logged in user's task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task was changed",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Task.class))}),
            @ApiResponse(responseCode = "400", description = "Task with such id doesn't exist", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized user can't edit tasks", content = @Content)
    })
    public ResponseEntity<TaskEditResponseDto> editTask(@PathVariable("task_title") String title,
                                                        @PathVariable("email") String email,
                                                        @RequestBody TaskEditRequestDto request) {
        return new ResponseEntity<>(taskService.editTaskByTitle(title, request, email), HttpStatus.OK);
    }

    @DeleteMapping("/{email}/{task_id}")
    @PreAuthorize("#email == principal.username")
    @Operation(summary = "Delete Task by provided task_id (may be acquired only by this task's creator)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task was deleted",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Task with such id doesn't exist", content = @Content),
            @ApiResponse(responseCode = "401", description =
                    "Unauthorized user can't delete tasks / Logged user cannot delete another user's tasks", content = @Content)
    })
    public ResponseEntity<?> deleteTask(@Parameter @PathVariable("task_id") @RequestParam String id,
                                        @PathVariable("email") String email) {
        taskService.deleteTask(id, email);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{email}/all")
    @PreAuthorize("#email == principal.username")
    @Operation(summary = "Get all user's existing Tasks (Pagination and sorting params may be including in request by User)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get all Tasks including pagination and sorting",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Task.class))}),
            @ApiResponse(responseCode = "401", description =
                    "Unauthorized user can't get tasks", content = @Content)
    })
    public List<TaskDto> getAllUserTasks(@Parameter @RequestParam(defaultValue = "0") Integer pageNum,
                                  @Parameter @RequestParam(defaultValue = "10") Integer pageSize,
                                  @PathVariable("email") String email) {
        return taskService.getAllUserTasks(pageNum, pageSize, email);
    }

    @GetMapping("/get-created/{author_email}")
    @Operation(summary = "Get all existing Tasks created by specified User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get all tasks by specific author",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Task.class))}),
            @ApiResponse(responseCode = "400", description = "Author with such id doesn't exist", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized user can't get tasks", content = @Content)
    })
    public List<TaskDto> getAllCreatedTasksByAuthorEmail(@Parameter @PathVariable("author_email") @RequestParam String email) {
        return taskService.getAllTasksCreatedByUser(email);
    }

    @GetMapping("/get-assigned/{performer_email}")
    @Operation(summary = "Get all existing Tasks performed by specified User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get all tasks by specific performer",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Task.class))}),
            @ApiResponse(responseCode = "400", description = "Performer with such id doesn't exist", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized user can't get tasks", content = @Content)
    })
    public List<TaskDto> getAllAssignedTasksByPerformerEmail(@Parameter @PathVariable("performer_email") @RequestParam String email) {
        return taskService.getAllTasksAssignedToUser(email);
    }
}
