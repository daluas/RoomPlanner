package edu.roomplanner.service.impl;

import edu.roomplanner.entity.UserEntity;
import edu.roomplanner.repository.UserRepository;
import edu.roomplanner.service.ValidationService;
import edu.roomplanner.types.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ValidationServiceImpl implements ValidationService {

    private UserRepository userRepository;

    @Autowired
    public ValidationServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean checkValidRoomId(Long id) {
        if(checkExistingRoomId(id)){
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
