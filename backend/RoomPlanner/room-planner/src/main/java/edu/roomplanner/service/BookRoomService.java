package edu.roomplanner.service;

import edu.roomplanner.dto.ReservationDto;
import edu.roomplanner.entity.ReservationEntity;

public interface BookRoomService {
    ReservationEntity createReservation(ReservationEntity reservationEntity);
    ReservationEntity convertToEntity(ReservationDto reservationDto);
    ReservationDto convertToDto(ReservationEntity reservationEntity);


}
