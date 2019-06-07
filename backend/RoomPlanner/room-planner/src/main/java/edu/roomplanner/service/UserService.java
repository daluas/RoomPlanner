package edu.roomplanner.service;

import edu.roomplanner.dto.RoomDto;
import edu.roomplanner.entity.UserEntity;

import java.util.List;

public interface UserService {

    List<RoomDto> getAllRooms();
    RoomDto getRoomById(Integer id);

}
