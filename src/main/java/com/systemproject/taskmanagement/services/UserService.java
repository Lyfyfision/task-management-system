package com.systemproject.taskmanagement.services;

import com.systemproject.taskmanagement.entities.Task;
import com.systemproject.taskmanagement.entities.User;

import java.util.List;

public interface UserService {
    User insertUser(User user);
    User getUser(Long id) throws Exception;
    User getUser(String email) throws Exception;
    User getTaskPerformerByTaskId(Long id);
    User getTaskAuthorByTaskId(Long id);
    List<User> getUsers();
}

