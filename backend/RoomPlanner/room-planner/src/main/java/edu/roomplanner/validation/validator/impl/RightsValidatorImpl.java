package edu.roomplanner.validation.validator.impl;

import edu.roomplanner.builders.UserDtoBuilder;
import edu.roomplanner.dto.RoomDto;
import edu.roomplanner.entity.RoomEntity;
import edu.roomplanner.entity.UserEntity;
import edu.roomplanner.mappers.RoomDtoMapper;
import edu.roomplanner.repository.UserRepository;
import edu.roomplanner.validation.validator.RightsValidator;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class RightsValidatorImpl implements RightsValidator {

    private UserRepository userRepository;
    private RoomDtoMapper roomDtoMapper;
    private UserDtoBuilder userDtoBuilder;

    @Override
    public boolean checkRoomReadRights(String email, Long id) {

        Optional<UserEntity> userEntity = userRepository.findByEmail(email);
        if (userEntity.isPresent()) {
            RoomDto roomDto = roomDtoMapper.mapEntityToDto((RoomEntity) userEntity.get());
            return roomDto.getId().equals(id);
        }
        return false;
    }
}

