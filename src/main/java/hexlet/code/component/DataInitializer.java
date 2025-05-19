package hexlet.code.component;

import hexlet.code.model.Label;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;

import jakarta.annotation.PostConstruct;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;

@Component
@AllArgsConstructor
public class DataInitializer {

  //  @Autowired
    private UserRepository userRepository;
  //  @Autowired
    private PasswordEncoder passwordEncoder;
  //  @Autowired
    private TaskStatusRepository taskStatusRepository;
  //  @Autowired
    private LabelRepository labelRepository;

// юзер по умолчанию hexlet@example.com qwerty ----------------------------------------
    @PostConstruct
    public void initializeUsers() {
        if (userRepository.findByEmail("hexlet@example.com").isEmpty()) {
            User user = new User();
            user.setEmail("hexlet@example.com");
            user.setPasswordDigest(passwordEncoder.encode("qwerty"));
            userRepository.save(user);
        }
    }
// Статусы тасков при старте ----------------------------------------------------------
    @PostConstruct
    public void initializeTaskStatuses() {
        List<String> statuses = Arrays.asList("draft", "to_review", "to_be_fixed", "to_publish", "published");
        statuses.forEach(status -> {
            var task = taskStatusRepository.findBySlug(status);
            task.ifPresentOrElse(x -> { }, () -> {
                TaskStatus taskStatus = new TaskStatus();
                taskStatus.setName(status.replace("_", " "));
                taskStatus.setSlug(status);
                taskStatusRepository.save(taskStatus);
            });
        });
    }
// Метки при старте ----------------------------------------------------------------
    @PostConstruct
    public void initializeLabels() {
        List<String> labels = Arrays.asList("feature", "bug");
        labels.forEach(label -> {
            var foundedLabel = labelRepository.findByName(label);
            foundedLabel.ifPresentOrElse(x -> {
            }, () -> {
                Label newLabel = new Label();
                newLabel.setName(label);
                labelRepository.save(newLabel);
            });
        });
    }
}
