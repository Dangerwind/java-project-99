package hexlet.code.service;

import hexlet.code.dto.UserCreateDTO;
import hexlet.code.dto.UserDTO;
import hexlet.code.dto.UserUpdateDTO;
import hexlet.code.mapper.UserMapper;
import hexlet.code.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import hexlet.code.exception.ResourceNotFoundException;



import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

//GET /api/users
    public List<UserDTO> index() {
        var users = userRepository.findAll();
        var ret = users.stream()
                .map(userMapper::map)
                .toList();
        System.out.println(" ");
        System.out.println("!!! " + ret + " !!!!!eee!!!");
        System.out.println(" ");
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

        System.out.println("!!!!!!!! cr !!!!!!!!!" + newUser + " cr !!!!!!!!");
        return userMapper.map(newUser);
    }
// PUT /api/users/{id}
    public UserDTO update(long id, UserUpdateDTO dto) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not Found"));
        userMapper.update(dto, user);
        if (dto.getPassword() != null) {
            user.setPasswordDigest(passwordEncoder.encode(dto.getPassword().get()));
        }
        userRepository.save(user);
        return userMapper.map(user);
    }

    public void delete(long id) {
        userRepository.deleteById(id);
    }


}
