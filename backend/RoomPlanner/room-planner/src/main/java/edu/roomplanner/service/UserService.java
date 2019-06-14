package edu.roomplanner.service;

import edu.roomplanner.dto.UserEmailTypeDto;
import edu.roomplanner.entity.UserEntity;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<UserEntity> getUsers();

    UserEntity saveUser(UserEntity userEntity);

    Optional<UserEmailTypeDto> getUserEmailTypeDto(String email);

}
