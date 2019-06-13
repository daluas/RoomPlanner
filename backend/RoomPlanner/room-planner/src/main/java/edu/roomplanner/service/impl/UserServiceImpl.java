package edu.roomplanner.service.impl;

import edu.roomplanner.dto.RoomDto;
import edu.roomplanner.entity.RoomEntity;
import edu.roomplanner.entity.UserEntity;
import edu.roomplanner.mappers.RoomDtoMapper;
import edu.roomplanner.repository.UserRepository;
import edu.roomplanner.service.UserService;
import edu.roomplanner.types.UserType;
import edu.roomplanner.validation.UserValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private UserValidation userValidation;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserValidation userValidation) {
        this.userRepository = userRepository;
        this.userValidation = userValidation;
    }

    @Override
    public List<RoomDto> getAllRooms() {
        List<UserEntity> roomEntities = userRepository.findByType(UserType.ROOM);
        return RoomDtoMapper.mapEntityListToDtoList(roomEntities);
    }

    @Override
    public RoomDto getRoomById(Long id) {
        RoomDto roomDto = null;
        if (userValidation.checkValidRoomId(id)) {
            UserEntity userEntity = userRepository.findById(id).get();
           roomDto = RoomDtoMapper.mapEntityToDto((RoomEntity) userEntity);
        }
        return roomDto;
    }

    @Override
    public List<RoomDto> getFilteredRooms() {
        return null;
    }

}
