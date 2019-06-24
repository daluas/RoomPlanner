package edu.roomplanner.service;

import edu.roomplanner.dto.RoomDto;
import edu.roomplanner.dto.UserDto;
import edu.roomplanner.entity.ReservationEntity;
import edu.roomplanner.entity.UserEntity;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserService {

    List<RoomDto> getAllRooms();

    RoomDto getRoomById(Long id);

    Optional<UserDto> getUserDto(String email);

    Set<ReservationEntity> updateReservationDescription(UserEntity userEntity);

}
