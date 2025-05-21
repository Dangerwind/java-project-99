package hexlet.code.dto.label;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class LabelDTO {

    private Long id;

    @NotNull
    @Size(min = 3, max = 1000)
    private String name;

    private LocalDate createdAt;
}
