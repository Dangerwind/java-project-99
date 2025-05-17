package hexlet.code.controller.api;

import hexlet.code.dto.task.TaskStatusCreateDTO;
import hexlet.code.dto.task.TaskStatusDTO;
import hexlet.code.dto.task.TaskStatusUpdateDTO;

import hexlet.code.service.TaskStatusService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/api/task_statuses")
@AllArgsConstructor
public class TaskStatusController {

    private TaskStatusService taskStatusService;

//GET /api/task_statuses/{id}
    @GetMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TaskStatusDTO show(@PathVariable long id) {
        return taskStatusService.show(id);
    }

//GET /api/task_statuses
    @GetMapping(path = "")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<TaskStatusDTO>> index() {
        var result = taskStatusService.index();
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(result.size()))
                .body(result);
    }

//POST /api/task_statuses
    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public TaskStatusDTO create(@RequestBody @Valid TaskStatusCreateDTO createDTO) {
        return taskStatusService.create(createDTO);
    }

//PUT /api/task_statuses/{id}
    @PutMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TaskStatusDTO update(@PathVariable Long id, @RequestBody @Valid TaskStatusUpdateDTO updateDTO) {
        return taskStatusService.update(id, updateDTO);
    }

//DELETE /api/task_statuses/{id}
    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {
        taskStatusService.delete(id);
    }

}
