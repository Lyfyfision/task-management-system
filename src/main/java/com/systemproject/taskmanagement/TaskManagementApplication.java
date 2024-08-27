package com.systemproject.taskmanagement;

import com.systemproject.taskmanagement.entities.User;
import com.systemproject.taskmanagement.repository.TaskRepository;
import com.systemproject.taskmanagement.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;

@SpringBootApplication
@AllArgsConstructor
public class TaskManagementApplication implements CommandLineRunner {

	TaskRepository taskRepository;
	UserRepository userRepository;
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	public static void main(String[] args) {
		SpringApplication.run(TaskManagementApplication.class, args);
	}
	@Override
	public void run(String... args) throws Exception {
		User[] users = new User[] {
				new User("User1@mail", passwordEncoder().encode("User11")),
				new User("User2@mail", passwordEncoder().encode("User22")),
				new User("User3@mail", passwordEncoder().encode("User33")),
				new User("User4@mail", passwordEncoder().encode("User44")),
				new User("User5@mail", passwordEncoder().encode("User55")),
				new User("User6@mail", passwordEncoder().encode("User66"))
		};
		userRepository.saveAll(Arrays.asList(users));

	}
}
