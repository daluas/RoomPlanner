package edu.roomplanner.mappers.impl;

import edu.roomplanner.dto.ReservationDto;
import edu.roomplanner.entity.ReservationEntity;
import edu.roomplanner.mappers.ReservationDtoMapper;
import org.springframework.stereotype.Service;

@Service
public class ReservationDtoMapperImpl implements ReservationDtoMapper {

    public ReservationDto mapReservationEntityToDto(ReservationEntity reservationEntity) {
        ReservationDto reservationDto = new ReservationDto();
        reservationDto.setStartDate(reservationEntity.getStartDate());
        reservationDto.setEndDate(reservationEntity.getEndDate());
        reservationDto.setDescription(reservationEntity.getDescription());
        return reservationDto;
    }
    public ReservationEntity mapReservationDtoToEntity(ReservationDto reservationDto) {
        ReservationEntity reservationEntity = new ReservationEntity();
        reservationEntity.setStartDate(reservationDto.getStartDate());
        reservationEntity.setEndDate(reservationDto.getEndDate());
        reservationEntity.setDescription(reservationDto.getDescription());
        return reservationEntity;
    }
}
