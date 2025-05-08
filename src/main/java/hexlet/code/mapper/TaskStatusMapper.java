package hexlet.code.mapper;


import hexlet.code.dto.task.TaskStatusCreateDTO;
import hexlet.code.dto.task.TaskStatusDTO;

import hexlet.code.dto.task.TaskStatusUpdateDTO;

import hexlet.code.model.TaskStatus;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
        uses = { JsonNullableMapper.class },
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)

public abstract class TaskStatusMapper {

    public abstract TaskStatus map(TaskStatusDTO dto);

    public abstract TaskStatusDTO map(TaskStatus taskStatus);

    public abstract TaskStatus map(TaskStatusCreateDTO dto);

    public abstract void update(TaskStatusUpdateDTO taskStatusUpdateDTO, @MappingTarget TaskStatus taskStatus);

}
