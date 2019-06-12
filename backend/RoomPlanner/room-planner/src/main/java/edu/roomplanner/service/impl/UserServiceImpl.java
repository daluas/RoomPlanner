package edu.roomplanner.service.impl;

import edu.roomplanner.dto.RoomDto;
import edu.roomplanner.entity.RoomEntity;
import edu.roomplanner.entity.UserEntity;
import edu.roomplanner.repository.UserRepository;
import edu.roomplanner.service.MapperService;
import edu.roomplanner.service.UserService;
import edu.roomplanner.types.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private MapperService mapperService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, MapperService mapperService) {
        this.userRepository = userRepository;
        this.mapperService = mapperService;
    }

    @Override
    public List<RoomDto> getAllRooms() {
        List<UserEntity> roomEntities = userRepository.findByType(UserType.ROOM);
        return mapperService.mapListToDto(roomEntities);
    }

    @Override
    public RoomDto getRoomById(Long id) {
        UserEntity userEntity = userRepository.findById(id).get();
        return mapperService.mapToDto((RoomEntity) userEntity);
    }

}
