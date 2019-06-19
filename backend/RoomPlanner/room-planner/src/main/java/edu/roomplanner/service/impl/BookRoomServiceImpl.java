package edu.roomplanner.service.impl;

import edu.roomplanner.dto.ReservationDto;
import edu.roomplanner.entity.ReservationEntity;
import edu.roomplanner.mappers.ReservationDtoMapper;
import edu.roomplanner.service.BookRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookRoomServiceImpl implements BookRoomService {
    private ReservationDtoMapper mapperService;
    private ReservationDto reservationDto;


    @Autowired
    public BookRoomServiceImpl(ReservationDtoMapper mapperService, ReservationDto reservationDto) {
        this.mapperService = mapperService;
        this.reservationDto = reservationDto;
    }

    @Override
    public ReservationEntity createReservation(ReservationEntity reservationEntity) {
        ReservationEntity newReservation = new ReservationEntity();
        newReservation.setStartDate(reservationEntity.getStartDate());
        newReservation.setEndDate(reservationEntity.getEndDate());
        newReservation.setDescription(reservationEntity.getDescription());
        newReservation.setRoom(reservationEntity.getRoom());
        newReservation.setPerson(reservationEntity.getPerson());

        return newReservation;

    }

    @Override
    public ReservationEntity convertToEntity(ReservationDto reservationDto) {
        return mapperService.mapReservationDtoToEntity(reservationDto);
    }

    @Override
    public ReservationDto convertToDto(ReservationEntity reservationEntity) {
        return mapperService.mapReservationEntityToDto(reservationEntity);
    }

}
