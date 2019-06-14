package edu.roomplanner.service.impl;


import edu.roomplanner.dto.RoomDto;
import edu.roomplanner.entity.RoomEntity;
import edu.roomplanner.entity.UserEntity;
import edu.roomplanner.mappers.RoomDtoMapper;
import edu.roomplanner.repository.UserRepository;
import edu.roomplanner.service.UserService;
import edu.roomplanner.types.UserType;
import edu.roomplanner.validation.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
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

}
