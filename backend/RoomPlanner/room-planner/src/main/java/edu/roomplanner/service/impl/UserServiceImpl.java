package edu.roomplanner.service.impl;

import edu.roomplanner.builders.FloorDtoBuilder;
import edu.roomplanner.dto.FloorDto;
import edu.roomplanner.builders.UserDtoBuilder;
import edu.roomplanner.dto.RoomDto;
import edu.roomplanner.dto.UserDto;
import edu.roomplanner.entity.PersonEntity;
import edu.roomplanner.entity.RoomEntity;
import edu.roomplanner.entity.UserEntity;
import edu.roomplanner.mappers.RoomDtoMapper;
import edu.roomplanner.repository.UserRepository;
import edu.roomplanner.service.UserService;
import edu.roomplanner.types.UserType;
import edu.roomplanner.validation.validator.UserValidator;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@Builder
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private UserValidator userValidator;
    private RoomDtoMapper roomDtoMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserValidator userValidator, RoomDtoMapper roomDtoMapper) {
        this.userRepository = userRepository;
        this.userValidator = userValidator;
        this.roomDtoMapper = roomDtoMapper;
    }

    @Override
    public List<RoomDto> getAllRooms() {
        List<UserEntity> roomEntities = userRepository.findByType(UserType.ROOM);
        return roomDtoMapper.mapEntityListToDtoList(roomEntities);
    }

    @Override
    public RoomDto getRoomById(Long id) {
        RoomDto roomDto = null;
        if (userValidator.checkValidRoomId(id)) {
            UserEntity userEntity = userRepository.findById(id).get();
            roomDto = roomDtoMapper.mapEntityToDto((RoomEntity) userEntity);
        }
        return roomDto;
    }

    @Override
    public Optional<UserDto> getUserDto(String email) {
        Optional<UserEntity> userEntity = userRepository.findByEmail(email);
        if (userEntity.isPresent()) {
            UserDto userDto = buildUserDto(userEntity.get());
            return Optional.of(userDto);
        }
        return Optional.empty();
    }

    private UserDto buildUserDto(UserEntity userEntity) {
        if (userEntity.getType() == UserType.PERSON) {
            return buildUserDtoByPersonEntity((PersonEntity) userEntity);
        }
        return buildUserDtoByRoomEntity((RoomEntity) userEntity);
    }

    private UserDto buildUserDtoByPersonEntity(PersonEntity personEntity) {
        return UserDtoBuilder.builder()
                .withId(personEntity.getId())
                .withEmail(personEntity.getEmail())
                .withType(personEntity.getType())
                .withFirstName(personEntity.getFirstName())
                .withLastName(personEntity.getLastName())
                .build();
    }

    private UserDto buildUserDtoByRoomEntity(RoomEntity roomEntity) {

        FloorDto floorDto = new FloorDtoBuilder()
                .withId(roomEntity.getFloor().getId())
                .withFloor(roomEntity.getFloor().getFloor())
                .build();

        return UserDtoBuilder.builder()
                .withId(roomEntity.getId())
                .withEmail(roomEntity.getEmail())
                .withType(roomEntity.getType())
                .withName(roomEntity.getName())
                .withFloor(floorDto)
                .withMaxPersons(roomEntity.getMaxPersons())
                .build();
    }

}
