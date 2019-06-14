package edu.roomplanner.mappers;

import edu.roomplanner.dto.ReservationDto;
import edu.roomplanner.entity.ReservationEntity;

public interface ReservationDtoMapper {
    ReservationDto mapReservationEntityToDto(ReservationEntity reservationEntity);
    ReservationEntity mapReservationDtoToEntity(ReservationDto reservationDto);
}
