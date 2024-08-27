package com.systemproject.taskmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A DTO for the {@link com.systemproject.taskmanagement.entities.User} entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private String id;
    private String email;
}
