package hexlet.code.component;



import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;


@Component
public class DataInitializer {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void initialize() {
        if (userRepository.findByEmail("hexlet@example.com").isEmpty()) {
            User user = new User();
            user.setEmail("hexlet@example.com");
            user.setPasswordDigest(passwordEncoder.encode("qwerty"));
            user.setFirstName("Hex111let");
            user.setLastName("Exa222mple");
            userRepository.save(user);
        }
    }
}
