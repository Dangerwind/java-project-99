package hexlet.code.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import hexlet.code.repository.UserRepository;

@Component
public class UserUtils {

    @Autowired
    private UserRepository userRepository;

    public boolean isAuthor(long userId) {
        var userAuthorEmail = userRepository.findById(userId).get().getEmail();
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        return userAuthorEmail.equals(authentication.getName());
    }
}


