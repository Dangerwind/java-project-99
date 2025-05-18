package hexlet.code.service;

import hexlet.code.dto.user.UserCreateDTO;
import hexlet.code.dto.user.UserDTO;
import hexlet.code.dto.user.UserUpdateDTO;
import hexlet.code.exception.ResourceDeletionException;
import hexlet.code.mapper.UserMapper;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import hexlet.code.exception.ResourceNotFoundException;

import java.util.List;

@AllArgsConstructor
@Service
public class UserService implements UserDetailsService {

    // @Autowired
    private UserRepository userRepository;

    //@Autowired
    private UserMapper userMapper;

    //@Autowired
    private PasswordEncoder passwordEncoder;
    //@Autowired
    private TaskRepository taskRepository;

    //GET /api/users
    public List<UserDTO> index() {
        var users = userRepository.findAll();
        var ret = users.stream()
                .map((user) -> userMapper.map(user))
                .toList();
        return ret;
    }
//GET /api/users/{id}
    public UserDTO show(long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not Found"));
        return userMapper.map(user);
    }

// POST /api/users
    public UserDTO create(UserCreateDTO dto) {
        var newUser = userMapper.map(dto);
        newUser.setPasswordDigest(passwordEncoder.encode(dto.getPassword()));
        userRepository.save(newUser);

        return userMapper.map(newUser);
    }
// PUT /api/users/{id}
    public UserDTO update(long id, UserUpdateDTO dto) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not Found"));
        userMapper.update(dto, user);
        if (dto.getPassword() != null && dto.getPassword().isPresent()) {
            user.setPasswordDigest(passwordEncoder.encode(dto.getPassword().get()));
        }
        userRepository.save(user);
        return userMapper.map(user);
    }

    public void delete(long id) {
        if (taskRepository.existsByAssigneeId(id)) {
            throw new ResourceDeletionException("Нельзя удалить пользователя, у него есть задача");
        }
        userRepository.deleteById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return user;
    }
}
