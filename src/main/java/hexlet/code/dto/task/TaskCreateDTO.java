package hexlet.code.dto.task;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

@Getter
@Setter
public class TaskCreateDTO {

    //private Long taskLabelIds;
    private Long index;
    private String title;
    private String content;
    private String status;

    @JsonProperty("assignee_id")
    private JsonNullable<Long> assigneeId;
}

