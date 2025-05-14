package hexlet.code.mapper;

import hexlet.code.dto.task.TaskCreateDTO;
import hexlet.code.dto.task.TaskDTO;

import hexlet.code.dto.task.TaskUpdateDTO;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.TaskStatusRepository;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;


@Mapper(
        uses = { JsonNullableMapper.class, ReferenceMapper.class },
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)

public abstract class TaskMapper {

    @Autowired
    private TaskStatusRepository statusRepository;

    @Mapping(target = "name", source = "title")
    @Mapping(target = "description", source = "content")
    @Mapping(target = "taskStatus", source = "status", qualifiedByName = "statusBySlug")
    @Mapping(target = "assignee", source = "assigneeId")
    public abstract Task map(TaskCreateDTO dto);

    @Mapping(target = "title", source = "name")
    @Mapping(target = "content", source = "description")
    @Mapping(target = "status", source = "taskStatus.slug")
    @Mapping(target = "assigneeId", source = "assignee.id")
    public abstract TaskDTO map(Task task);


    @Mapping(target = "name", source = "title")
    @Mapping(target = "description", source = "content")
    @Mapping(target = "taskStatus", source = "status", qualifiedByName = "statusBySlug")
    @Mapping(target = "assignee", source = "assigneeId")
    public abstract Task map(TaskDTO dto);


    @Mapping(target = "name", source = "title")
    @Mapping(target = "description", source = "content")
    @Mapping(target = "assignee", source = "assigneeId")
    @Mapping(target = "taskStatus", source = "status", qualifiedByName = "statusBySlug")
    public abstract void update(TaskUpdateDTO dto, @MappingTarget Task task);

    @Named("statusBySlug")
    public TaskStatus statusBySlug(String slug) {
        return statusRepository.findBySlug(slug).orElseThrow();
    }

}
