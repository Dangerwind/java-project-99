package hexlet.code.util;

import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import net.datafaker.Faker;
import org.instancio.Instancio;
import org.instancio.Model;
import org.instancio.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;


@Getter
@Component
public class ModelGenerator {

    private Model<User> userModel;
    private Model<TaskStatus> taskStatusModel;

    @Autowired
    private Faker faker;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    private void init() {
        userModel = Instancio.of(User.class)
                .ignore(Select.field(User::getId))
                .supply(Select.field(User::getEmail), () -> faker.internet().emailAddress())
                .supply(Select.field(User::getFirstName), () -> faker.name().firstName())
                .supply(Select.field(User::getLastName), () -> faker.name().lastName())
                .supply(Select.field(User.class, "passwordDigest"), () -> faker.internet().password(3, 10))
                .toModel();

        taskStatusModel = Instancio.of(TaskStatus.class)
                .ignore(Select.field(TaskStatus::getId))
                .supply(Select.field(TaskStatus::getName), () -> faker.lorem().words(2).stream()
                        .map(w -> w.toLowerCase().replaceAll("[^a-z0-9]", ""))
                        .collect(Collectors.joining(" ")))
                .supply(Select.field(TaskStatus::getSlug), () -> faker.lorem().words(2).stream()
                        .map(w -> w.toLowerCase().replaceAll("[^a-z0-9]", ""))
                        .collect(Collectors.joining("_")))
                .toModel();
    }

}
