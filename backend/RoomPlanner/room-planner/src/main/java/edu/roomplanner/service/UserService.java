package edu.roomplanner.service;

import edu.roomplanner.dto.RoomDto;
import edu.roomplanner.dto.UserDto;
import edu.roomplanner.entity.ReservationEntity;
import edu.roomplanner.entity.UserEntity;

import java.util.Calendar;
import java.util.List;
import java.util.Set;

public interface UserService {

    List<RoomDto> getAllRooms();

    RoomDto getRoomById(Long id);

    RoomDto getRoomByEmail(String email);

    UserDto getUserDto(String email);

    List<RoomDto> getRoomsByFilters(Calendar startDate, Calendar endDate, Integer minPersons, Integer floor);

    Set<ReservationEntity> updateReservationDescription(Set<ReservationEntity> reservationEntities);

    List<UserEntity> updateUserEntitiesReservation(List<UserEntity> userEntities);
}

