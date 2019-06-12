package edu.roomplanner.service;

import edu.roomplanner.dto.RoomDto;

import java.util.List;

public interface UserService {

    List<RoomDto> getAllRooms();

    RoomDto getRoomById(Long id);

    List<RoomDto> getFiltredRooms();

}
