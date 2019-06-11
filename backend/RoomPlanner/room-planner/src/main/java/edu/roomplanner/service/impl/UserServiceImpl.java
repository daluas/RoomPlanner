package edu.roomplanner.service.impl;

import edu.roomplanner.dto.RoomDto;
import edu.roomplanner.entity.RoomEntity;
import edu.roomplanner.entity.UserEntity;
import edu.roomplanner.repository.UserRepository;
import edu.roomplanner.service.UserService;
import edu.roomplanner.types.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<RoomDto> getAllRooms() {
        List<UserEntity> roomEntities = userRepository.findByType(UserType.ROOM);
        return RoomDto.mapListToDto(roomEntities);
    }

    @Override
    public RoomDto getRoomById(Long id) {
        UserEntity userEntity = userRepository.findById(id).get();
        return RoomDto.mapToDto((RoomEntity) userEntity);
    }

}
