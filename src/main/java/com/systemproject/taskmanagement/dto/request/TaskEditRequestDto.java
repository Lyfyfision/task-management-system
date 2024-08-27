package com.systemproject.taskmanagement.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskEditRequestDto {
    private String newTitle;
    private LocalDate newDate;
    private String newDescription;
}
