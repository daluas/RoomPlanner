package edu.roomplanner.service;

import edu.roomplanner.dto.ReservationDto;
import edu.roomplanner.entity.ReservationEntity;
import org.springframework.http.HttpStatus;

public interface ReservationService {

    ReservationDto createReservation(Long roomId, ReservationDto reservationDto);

    ReservationEntity convertToEntity(ReservationDto reservationDto);

    ReservationDto convertToDto(ReservationEntity reservationEntity);

    void deleteReservation(Long reservationId);

}
