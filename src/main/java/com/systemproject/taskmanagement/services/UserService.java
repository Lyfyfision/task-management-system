package com.systemproject.taskmanagement.services;

import com.systemproject.taskmanagement.entities.Task;
import com.systemproject.taskmanagement.entities.User;

import java.util.List;

public interface UserService {
    User insertUser(User user);
    User getUserById(String id) throws Exception;
    User getUserByEmail(String email) throws Exception;
    User getTaskPerformerByTaskId(String id);
    User getTaskAuthorByTaskId(String id);
    List<User> getUsers();
}

