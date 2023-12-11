package com.systemproject.taskmanagement.controllers;

import com.systemproject.taskmanagement.entities.Task;
import com.systemproject.taskmanagement.entities.User;
import com.systemproject.taskmanagement.services.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {
    private final UserServiceImpl userService;

    @PostMapping("/register")
    @Operation(summary = "Simple User registration (provided by email and password)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User was successfully registered",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = User.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid user details", content = @Content)
    })
    public ResponseEntity<User> registerUser(@ParameterObject @Valid @RequestBody User user) {
        userService.insertUser(user);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }
    @GetMapping("/get-all")
    public List<User> getAllUser() {
        return userService.getUsers();
    }
}
