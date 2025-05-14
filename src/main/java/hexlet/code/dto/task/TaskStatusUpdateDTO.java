package hexlet.code.dto.task;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

@Getter
@Setter

public class TaskStatusUpdateDTO {

    @NotNull
    @NotBlank
    private JsonNullable<String> name;

    @NotNull
    @NotBlank
    private JsonNullable<String> slug;
}
