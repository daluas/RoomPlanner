package edu.roomplanner.validation.validator.impl;

import edu.roomplanner.entity.UserEntity;
import edu.roomplanner.repository.UserRepository;
import edu.roomplanner.service.TokenParserService;
import edu.roomplanner.types.UserType;
import edu.roomplanner.validation.validator.UserRightsValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserRightsValidatorImpl implements UserRightsValidator {

    private TokenParserService tokenParserService;
    private UserRepository userRepository;

    @Autowired
    public UserRightsValidatorImpl(TokenParserService tokenParserService, UserRepository userRepository) {
        this.tokenParserService = tokenParserService;
        this.userRepository = userRepository;
    }

    @Override
    public boolean isUserLoggedAsRoom() {
        String email = tokenParserService.getEmailFromToken();
        Optional<UserEntity> userEntity = userRepository.findByEmail(email);
        return userEntity.filter(userEntity1 -> (userEntity1.getType() == UserType.ROOM))
                .isPresent();
    }

    @Override
    public boolean isLoggedRoomARequestedRoom(Long id) {
        String email = tokenParserService.getEmailFromToken();
        Optional<UserEntity> userEntity = userRepository.findByEmail(email);
        return userEntity.map(userEntity1 -> (userEntity1.getId().equals(id)))
                .orElse(false);
    }

}