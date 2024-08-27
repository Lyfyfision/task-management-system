package com.systemproject.taskmanagement.dto.mapper;

import com.systemproject.taskmanagement.dto.TaskDto;
import com.systemproject.taskmanagement.entities.Task;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface TaskMapper1 {
    Task toEntity(TaskDto taskDto);
    @Mappings({
            @Mapping(target = "title", source = "task.title"),
            @Mapping(target = "description", source = "task.description"),
            @Mapping(target = "creationDate", source = "task.creationDate"),
            @Mapping(target = "status", source = "task.taskStatus"),
            @Mapping(target = "priority", source = "task.taskPriority"),
            @Mapping(target = "comment", source = "task.comment"),
            @Mapping(target = "authorEmail", source = "task.author.email"),
            @Mapping(target = "performerEmail", source = "task.performer.email")
    })
    TaskDto toDto(Task task);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Task partialUpdate(TaskDto taskDto, @MappingTarget Task task);
}