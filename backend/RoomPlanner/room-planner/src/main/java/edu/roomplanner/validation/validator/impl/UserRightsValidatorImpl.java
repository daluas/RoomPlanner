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
    public boolean checkIfUserIsRoom() {
        String email = tokenParserService.getEmailFromToken();
        Optional<UserEntity> userEntity = userRepository.findByEmail(email);
        if (userEntity.isPresent()) {
            return (userEntity.get().getType() == UserType.ROOM);
        }
        return false;
    }

    @Override
    public boolean checkIfLoggedRoomIsRequestedRoom(Long id) {
        String email = tokenParserService.getEmailFromToken();
        Optional<UserEntity> userEntity = userRepository.findByEmail(email);
        if (userEntity.isPresent()) {
            return (userEntity.get().getId().equals(id));
        }
        return false;
    }

}