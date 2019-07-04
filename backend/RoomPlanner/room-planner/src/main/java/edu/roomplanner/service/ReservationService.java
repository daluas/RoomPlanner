package edu.roomplanner.service;

import edu.roomplanner.dto.ReservationDto;
import edu.roomplanner.entity.ReservationEntity;

public interface ReservationService {

    ReservationDto createReservation(Long roomId, ReservationDto reservationDto);

    ReservationDto updateReservation(Long roomId, ReservationDto reservationDto);

    void deleteReservation(Long reservationId);

}
