package com.systemproject.taskmanagement.services;

import com.systemproject.taskmanagement.entities.Task;
import com.systemproject.taskmanagement.entities.User;
import com.systemproject.taskmanagement.repository.TaskRepository;
import com.systemproject.taskmanagement.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static com.systemproject.taskmanagement.services.TaskServiceImpl.unwrapTask;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService{

    private UserRepository userRepository;
    private TaskRepository taskRepository;
    private BCryptPasswordEncoder encoder;

    @Override
    public User insertUser(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User getUser(Long id) {
        Optional<User> user = userRepository.findById(id);
        return unwrapUser(user);
    }

    @Override
    public User getUser(String email) {
        Optional<User> user = userRepository.findUserByEmail(email);
        return unwrapUser(user);
    }

    @Override
    public User getTaskPerformerByTaskId(Long id) {
        Optional<Task> task = taskRepository.findById(id);
        return unwrapTask(task).getPerformer();
    }

    @Override
    public User getTaskAuthorByTaskId(Long id) {
        Optional<Task> task = taskRepository.findById(id);
        return unwrapTask(task).getAuthor();
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll(Sort.by(Sort.Direction.DESC));
    }

    static User unwrapUser(Optional<User> entity) {
        if (entity.isPresent()) return entity.get();
        else throw new NoSuchElementException();
    }
}
