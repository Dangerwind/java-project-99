package hexlet.code.specification;

import hexlet.code.dto.task.TaskParamsDTO;
import hexlet.code.model.Task;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class TaskSpecification {

    public Specification<Task> build(TaskParamsDTO dto) {
        return withTitleCont(dto.getTitleCont())
                .and(withAssigneeId(dto.getAssigneeId()))
                .and(withStatus(dto.getStatus()));

    }

    private Specification<Task> withTitleCont(String titleCont) {
        return (root, query, cb) ->
                titleCont == null ?
                        cb.conjunction()
                        : cb.like(cb.lower(root.get("name")), "%" + titleCont.toLowerCase() + "%");
    }

    private Specification<Task> withAssigneeId(Long assigneeId) {
        return (root, query, cb) ->
                assigneeId == null ?
                        cb.conjunction()
                        : cb.equal(root.get("assignee").get("id"), assigneeId);
    }

    private Specification<Task> withStatus(String status) {
        return (root, query, cb) ->
                status == null ?
                        cb.conjunction()
                        : cb.equal(root.get("taskStatus").get("slug"), status);
    }
}
