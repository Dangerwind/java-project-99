package hexlet.code.dto;

import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

@Getter
@Setter
public class UserUpdateDTO {

    private JsonNullable<String> email;
    private JsonNullable<String> firstName;
    private JsonNullable<String> lastName;

    private JsonNullable<String> password;
}
