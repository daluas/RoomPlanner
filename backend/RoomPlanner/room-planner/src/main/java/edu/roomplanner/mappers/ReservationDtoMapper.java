package edu.roomplanner.mappers;

import edu.roomplanner.dto.ReservationDto;
import edu.roomplanner.entity.PersonEntity;
import edu.roomplanner.entity.ReservationEntity;
import edu.roomplanner.entity.RoomEntity;
import edu.roomplanner.entity.UserEntity;

public interface ReservationDtoMapper {

    ReservationEntity mapReservationDtoToEntity(ReservationDto reservationDto, UserEntity personEntity, UserEntity roomEntity);

    ReservationDto mapReservationEntityToDto(ReservationEntity reservationEntity, String personEmail);

}
