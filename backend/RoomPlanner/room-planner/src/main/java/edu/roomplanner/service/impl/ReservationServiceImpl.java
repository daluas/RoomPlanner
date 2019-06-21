package edu.roomplanner.service.impl;

import edu.roomplanner.dto.ReservationDto;
import edu.roomplanner.entity.ReservationEntity;
import edu.roomplanner.mappers.ReservationDtoMapper;
import edu.roomplanner.repository.ReservationRepository;
import edu.roomplanner.repository.UserRepository;
import edu.roomplanner.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ReservationServiceImpl implements ReservationService {

    private ReservationDtoMapper mapperService;
    private UserRepository userRepository;
    private ReservationRepository reservationRepository;

    @Autowired
    public ReservationServiceImpl(ReservationDtoMapper mapperService, UserRepository userRepository, ReservationRepository reservationRepository) {
        this.mapperService = mapperService;
        this.userRepository = userRepository;
        this.reservationRepository = reservationRepository;
    }

    @Override
    public Optional<ReservationDto> createReservation(Long roomId, ReservationDto reservationDto) {
        reservationDto.setRoomId(roomId);
        ReservationEntity reservationEntity = convertToEntity(reservationDto);
        if (reservationEntity.getPerson() == null || reservationEntity.getRoom() == null) {
            return Optional.empty();
        }
        reservationRepository.save(reservationEntity);
        ReservationDto currentReservationDto = convertToDto(reservationEntity);
        currentReservationDto.setRoomId(roomId);

        return Optional.of(currentReservationDto);
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
