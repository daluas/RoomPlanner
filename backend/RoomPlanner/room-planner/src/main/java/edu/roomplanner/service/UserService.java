package edu.roomplanner.service;

import edu.roomplanner.entity.UserEntity;

import java.util.List;

public interface UserService {

    List<UserEntity> getUsers();

    UserEntity saveUser(UserEntity userEntity);

}
