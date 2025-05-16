package hexlet.code.dto.task;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

import java.util.Set;

@Getter
@Setter
public class TaskUpdateDTO {

   // private JsonNullable<Long> taskLabelIds;
    private JsonNullable<Long> index;

    @JsonProperty("assignee_id")
    private JsonNullable<Long> assigneeId;

  //  @NotBlank
  //  @NotNull
    public JsonNullable<String> title;
    public JsonNullable<String> content;

 //   @NotBlank
    public JsonNullable<String> status;

    private JsonNullable<Set<Long>> taskLabelIds;
}
