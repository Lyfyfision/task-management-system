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
import java.util.UUID;

import static com.systemproject.taskmanagement.services.TaskServiceImpl.unwrapTask;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService{

    private UserRepository userRepository;
    private TaskRepository taskRepository;
    private BCryptPasswordEncoder encoder;

    //TODO: add check for existing users
    @Override
    public User insertUser(User user) {
        Optional<User> userFromDB = userRepository.findByEmail(user.getEmail());
        if (userFromDB.isPresent()) {
            throw new RuntimeException("Юзер с такой почтой уже существует");
        }
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User getUserById(String id) {
        Optional<User> user = userRepository.findById(UUID.fromString(id));
        return unwrapUser(user);
    }

    @Override
    public User getUserByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return unwrapUser(user);
    }

    @Override
    public User getTaskPerformerByTaskId(String id) {
        Optional<Task> task = taskRepository.findById(UUID.fromString(id));
        return unwrapTask(task).getPerformer();
    }

    @Override
    public User getTaskAuthorByTaskId(String id) {
        Optional<Task> task = taskRepository.findById(UUID.fromString(id));
        return unwrapTask(task).getAuthor();
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    static User unwrapUser(Optional<User> entity) {
        if (entity.isPresent()) return entity.get();
        else throw new NoSuchElementException();
    }
}
