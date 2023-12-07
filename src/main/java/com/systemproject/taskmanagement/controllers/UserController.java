package com.systemproject.taskmanagement.controllers;

import com.systemproject.taskmanagement.entities.User;
import com.systemproject.taskmanagement.services.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
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
    public ResponseEntity<?> registerUser(@Valid @RequestBody User user) {
        userService.insertUser(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @GetMapping("/get-all")
    public List<User> getAllUser() {
        return userService.getUsers();
    }
}
