package edu.roomplanner.service;

import edu.roomplanner.dto.ReservationDto;
import edu.roomplanner.entity.ReservationEntity;
import edu.roomplanner.entity.UserEntity;

import java.util.Optional;

public interface BookRoomService {

    Optional<ReservationDto>  createReservation(Long roomId, ReservationDto reservationDto);

    ReservationEntity convertToEntity(ReservationDto reservationDto, UserEntity personEntity, UserEntity roomEntity);

    ReservationDto convertToDto(ReservationEntity reservationEntity, String personEmail);

}
