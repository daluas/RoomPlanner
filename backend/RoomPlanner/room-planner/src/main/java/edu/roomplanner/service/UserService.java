package edu.roomplanner.service;

import edu.roomplanner.dto.RoomDto;
import edu.roomplanner.dto.UserDto;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<RoomDto> getAllRooms();

    RoomDto getRoomById(Long id);

    RoomDto getRoomByEmail(String email);

    Optional<UserDto> getUserDto(String email);

}
