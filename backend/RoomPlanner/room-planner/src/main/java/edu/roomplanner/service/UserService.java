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

    RoomDto getRoomByEmail(String email);

    Optional<UserDto> getUserDto(String email);

    Set<ReservationEntity> updateReservationDescription(Set<ReservationEntity> reservationEntities);

    List<UserEntity> updateUserEntitiesReservation(List<UserEntity> userEntities);
}

