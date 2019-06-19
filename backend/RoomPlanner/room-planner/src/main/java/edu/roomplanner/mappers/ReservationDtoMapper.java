package edu.roomplanner.mappers;

import edu.roomplanner.dto.ReservationDto;
import edu.roomplanner.entity.ReservationEntity;

public interface ReservationDtoMapper {
    ReservationEntity mapReservationDtoToEntity(ReservationDto reservationDto);
    ReservationDto mapReservationEntityToDto(ReservationEntity reservationEntity);

}
