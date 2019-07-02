package edu.roomplanner.service;

import edu.roomplanner.dto.ReservationDto;
import edu.roomplanner.entity.ReservationEntity;

public interface ReservationService {

    ReservationDto createReservation(Long roomId, ReservationDto reservationDto);

    ReservationEntity convertToEntity(ReservationDto reservationDto);

    ReservationDto convertToDto(ReservationEntity reservationEntity);

    ReservationDto updateReservation(Long roomId, ReservationDto reservationDto);

}
