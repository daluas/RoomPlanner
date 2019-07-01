package edu.roomplanner.service;

import edu.roomplanner.dto.ReservationDto;
import edu.roomplanner.entity.ReservationEntity;
import org.springframework.http.HttpStatus;

import java.util.Optional;

public interface ReservationService {

    Optional<ReservationDto> createReservation(Long roomId, ReservationDto reservationDto);

    ReservationEntity convertToEntity(ReservationDto reservationDto);

    ReservationDto convertToDto(ReservationEntity reservationEntity);

    HttpStatus deleteReservation(Long reservationId);

}
