package hexlet.code.mapper;

import hexlet.code.dto.task.TaskCreateDTO;
import hexlet.code.dto.task.TaskDTO;

import hexlet.code.dto.task.TaskUpdateDTO;
import hexlet.code.model.Label;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskStatusRepository;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;


@Mapper(
        uses = { JsonNullableMapper.class, ReferenceMapper.class },
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)

public abstract class TaskMapper {

    @Autowired
    private TaskStatusRepository statusRepository;

    @Autowired
    private LabelRepository labelRepository;

    @Mapping(target = "name", source = "title")
    @Mapping(target = "description", source = "content")
    @Mapping(target = "taskStatus", source = "status", qualifiedByName = "statusBySlug")
    @Mapping(target = "assignee", source = "assigneeId")
    @Mapping(target = "labels" ,source = "taskLabelIds", qualifiedByName = "getLabels")
    public abstract Task map(TaskCreateDTO dto);

    @Mapping(target = "title", source = "name")
    @Mapping(target = "content", source = "description")
    @Mapping(target = "status", source = "taskStatus.slug")
    @Mapping(target = "assigneeId", source = "assignee.id")
    @Mapping(target = "taskLabelIds" ,source = "labels", qualifiedByName = "getLabelIds")
    public abstract TaskDTO map(Task task);


    @Mapping(target = "name", source = "title")
    @Mapping(target = "description", source = "content")
    @Mapping(target = "taskStatus", source = "status", qualifiedByName = "statusBySlug")
    @Mapping(target = "assignee", source = "assigneeId")
    @Mapping(target = "labels" ,source = "taskLabelIds", qualifiedByName = "getLabels")
    public abstract Task map(TaskDTO dto);

    @Mapping(target = "name", source = "title")
    @Mapping(target = "description", source = "content")
    @Mapping(target = "assignee", source = "assigneeId")
    @Mapping(target = "taskStatus", source = "status", qualifiedByName = "statusBySlug")
    @Mapping(target = "labels" ,source = "taskLabelIds", qualifiedByName = "getLabels")
    public abstract void update(TaskUpdateDTO dto, @MappingTarget Task task);

    @Named("statusBySlug")
    public TaskStatus statusBySlug(String slug) {
        return statusRepository.findBySlug(slug).orElseThrow();
    }

    @Named("getLabelIds")
    public Set<Long> getLabelIds(Set<Label> labels) {
        return labels == null ? new HashSet<>()
                : labels.stream()
                .map(Label::getId)
                .collect(Collectors.toSet());
    }

    @Named("getLabels")
    Set<Label> getLabels(Set<Long> labelIds) {
        return labelRepository.findByIdIn(labelIds);
    }

}
