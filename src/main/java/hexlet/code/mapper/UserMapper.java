package hexlet.code.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import hexlet.code.model.User;

import hexlet.code.dto.UserDTO;
import hexlet.code.dto.UserCreateDTO;
import hexlet.code.dto.UserUpdateDTO;
//import org.springframework.beans.factory.annotation.Autowired;

//import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.crypto.password.PasswordEncoder;


@Mapper(
        uses = { JsonNullableMapper.class },
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)


public abstract class UserMapper {
    @Mapping(target = "passwordDigest", source = "password")
    public abstract User map(UserCreateDTO user);

    @Mapping(target = "password", source = "passwordDigest")
    public abstract UserCreateDTO mapCreate(User user);

    public abstract User map(UserDTO user);


    public abstract UserDTO map(User user);

    public abstract void update(UserUpdateDTO userUpdateDTO, @MappingTarget User user);

//        @Autowired
//    private PasswordEncoder encoder;

}
