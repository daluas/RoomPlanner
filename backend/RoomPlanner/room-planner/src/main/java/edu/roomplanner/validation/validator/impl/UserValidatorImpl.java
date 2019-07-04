package edu.roomplanner.validation.validator.impl;

import edu.roomplanner.entity.UserEntity;
import edu.roomplanner.repository.UserRepository;
import edu.roomplanner.types.UserType;
import edu.roomplanner.validation.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserValidatorImpl implements UserValidator {

    private UserRepository userRepository;

    @Autowired
    public UserValidatorImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean isRoomIdValid(Long id) {
        Optional<UserEntity> userEntity = userRepository.findById(id);
        return userEntity.filter(userEntity1 -> UserType.ROOM.equals(userEntity1.getType()))
                .isPresent();
    }

    @Override
    public boolean isRoomEmailValid(String email) {
        Optional<UserEntity> userEntity = userRepository.findByEmail(email);
        return userEntity.filter(userEntity1 -> UserType.ROOM.equals(userEntity1.getType()))
                .isPresent();
    }

}