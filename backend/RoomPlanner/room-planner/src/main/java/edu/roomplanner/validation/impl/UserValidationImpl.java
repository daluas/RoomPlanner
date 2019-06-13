package edu.roomplanner.validation.impl;

import edu.roomplanner.entity.UserEntity;
import edu.roomplanner.repository.UserRepository;
import edu.roomplanner.types.UserType;
import edu.roomplanner.validation.UserValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserValidationImpl implements UserValidation {

    private UserRepository userRepository;

    @Autowired
    public UserValidationImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean checkValidRoomId(Long id) {
        if (checkExistingRoomId(id)) {
            UserEntity userEntity = userRepository.findById(id).get();
            return UserType.ROOM.equals(userEntity.getType());
        }
        return false;
    }

    @Override
    public boolean checkExistingRoomId(Long id) {
        return userRepository.findById(id).isPresent();
    }
}
