package edu.roomplanner.service.impl;

import edu.roomplanner.dto.UserEmailTypeDto;
import edu.roomplanner.entity.UserEntity;
import edu.roomplanner.repository.UserRepository;
import edu.roomplanner.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<UserEntity> getUsers() {
        return (List<UserEntity>) userRepository.findAll();
    }

    @Override
    public UserEntity saveUser(UserEntity userEntity) {
        return userRepository.save(userEntity);
    }

    @Override
    public Optional<UserEmailTypeDto> getUserEmailTypeDto(String email) {
        Optional<UserEntity> currentUserEntity = userRepository.findByEmail(email);
        if(currentUserEntity.isPresent()){
            UserEmailTypeDto userEmailTypeDto = UserEmailTypeDto.builder()
                    .email(currentUserEntity.get().getEmail())
                    .type(currentUserEntity.get().getType())
                    .build();
            return Optional.of(userEmailTypeDto);
        }
        return Optional.empty();
    }

}
