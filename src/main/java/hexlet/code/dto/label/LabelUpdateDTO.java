package hexlet.code.dto.label;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

@Getter
@Setter
public class LabelUpdateDTO {

    @NotNull
    @Size(min = 3, max = 1000)
    @Column(unique = true)
    private JsonNullable<String> name;
}
