package hexlet.code.dto.task;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;


import java.time.LocalDate;
import java.util.Set;


@Getter
@Setter
public class TaskDTO {

    private Long id;
   // private Long taskLabelIds;
    private Long index;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate createdAt;

    @JsonProperty("assignee_id")
    private JsonNullable<Long> assigneeId;

    private String title;
    private String content;
    private String status;

    private Set<Long> taskLabelIds;
}
