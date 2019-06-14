package edu.roomplanner.mappers.impl;

import edu.roomplanner.dto.ReservationDto;
import edu.roomplanner.entity.ReservationEntity;
import edu.roomplanner.mappers.ReservationDtoMapper;
import org.springframework.stereotype.Service;

@Service
public class ReservationDtoMapperImpl implements ReservationDtoMapper {

    public ReservationEntity mapReservationDtoToEntity(ReservationDto reservationDto) {
        ReservationEntity reservationEntity = new ReservationEntity();
        reservationEntity.setId(reservationDto.getId());
        reservationEntity.setStartDate(reservationDto.getStartDate());
        reservationEntity.setEndDate(reservationDto.getEndDate());
        reservationEntity.setDescription(reservationDto.getDescription());
        return reservationEntity;
    }
}
