package hexlet.code.service;

import hexlet.code.dto.task.TaskStatusCreateDTO;
import hexlet.code.dto.task.TaskStatusDTO;
import hexlet.code.dto.task.TaskStatusUpdateDTO;

import hexlet.code.exception.ResourceDeletionException;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.TaskStatusMapper;

import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;
@AllArgsConstructor
@Service
public class TaskStatusService {

    private TaskStatusRepository taskStatusRepository;
    private TaskStatusMapper taskStatusMapper;
    private TaskRepository taskRepository;


    //GET /api/task_statuses/{id}
    public TaskStatusDTO show(long id) {
        var taskStatuses = taskStatusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not Found"));
        return taskStatusMapper.map(taskStatuses);
    }

    //GET /api/task_statuses
    public List<TaskStatusDTO> index() {
        var taskStatuses = taskStatusRepository.findAll();
        var ret = taskStatuses.stream()
                .map((task) -> taskStatusMapper.map(task))
                .toList();
        return ret;
    }

    // POST /api/task_statuses
    public TaskStatusDTO create(TaskStatusCreateDTO dto) {
        var newTaskStatus = taskStatusMapper.map(dto);
        taskStatusRepository.save(newTaskStatus);
        return taskStatusMapper.map(newTaskStatus);
    }

    // PUT /api/task_statuses/{id}
    public TaskStatusDTO update(long id, TaskStatusUpdateDTO dto) {
        var taskStatus = taskStatusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not Found"));
        taskStatusMapper.update(dto, taskStatus);
        taskStatusRepository.save(taskStatus);
        return taskStatusMapper.map(taskStatus);
    }

    public void delete(long id) {
        var taskStatus = taskStatusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not Found"));

        if (taskRepository.existsByTaskStatus(taskStatus)) {
            throw new ResourceDeletionException("Нельзя удалить статус, он связан с задачей");
        }
        taskStatusRepository.deleteById(id);
    }
}
