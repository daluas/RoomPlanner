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
    public ReservationEntity save(Long id) {
        return mapperService.mapReservationDtoToEntity(reservationDto);
    }

}
