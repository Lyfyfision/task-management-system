package com.systemproject.taskmanagement.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "e-mail", unique = true, nullable = false)
    @NotBlank
    @NonNull
    @Email
    private String email;

    @Column(name = "password", nullable = false)
    @NotBlank
    @NonNull
    @Size(min = 6, message = "Password must contain at least 6 characters")
    private String password;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "author")
    @JsonIgnore
    private List<Task> createdTasks;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "performer")
    @JsonIgnore
    private List<Task> assignedTasks;

}
