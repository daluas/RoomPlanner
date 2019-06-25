package edu.roomplanner.service;

import edu.roomplanner.dto.RoomDto;
import edu.roomplanner.dto.UserDto;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

public interface UserService {

    List<RoomDto> getAllRooms();

    RoomDto getRoomById(Long id);

    Optional<UserDto> getUserDto(String email);

    List<RoomDto> getRoomsByFilters(Calendar startDate, Calendar endDate, Integer minPersons, Integer floor);

}
