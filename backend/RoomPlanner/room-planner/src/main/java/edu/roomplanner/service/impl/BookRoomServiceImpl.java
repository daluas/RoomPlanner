package edu.roomplanner.service.impl;

import edu.roomplanner.dto.ReservationDto;
import edu.roomplanner.entity.ReservationEntity;
import edu.roomplanner.entity.UserEntity;
import edu.roomplanner.mappers.ReservationDtoMapper;
import edu.roomplanner.repository.ReservationRepository;
import edu.roomplanner.repository.UserRepository;
import edu.roomplanner.service.BookRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BookRoomServiceImpl implements BookRoomService {

    private ReservationDtoMapper mapperService;
    private ReservationDto reservationDto;
    private UserRepository userRepository;
    private ReservationRepository reservationRepository;

    @Autowired
    public BookRoomServiceImpl(ReservationDtoMapper mapperService, ReservationDto reservationDto, UserRepository userRepository, ReservationRepository reservationRepository) {
        this.mapperService = mapperService;
        this.reservationDto = reservationDto;
        this.userRepository = userRepository;
        this.reservationRepository = reservationRepository;
    }

    @Override
    public Optional<ReservationDto> createReservation(Long roomId, ReservationDto reservationDto) {
        Optional<UserEntity> roomEntityOptional = userRepository.findById(roomId);
        Optional<UserEntity> personEntityOptional = userRepository.findByEmail(reservationDto.getPersonEmail());

        if(roomEntityOptional.isPresent() && personEntityOptional.isPresent()) {
            ReservationEntity reservationEntity = convertToEntity(reservationDto, personEntityOptional.get(), roomEntityOptional.get());
            reservationRepository.save(reservationEntity);
            ReservationDto currentReservationDto = convertToDto(reservationEntity, reservationDto.getPersonEmail());

            return Optional.of(currentReservationDto);
        }

        return Optional.empty();
    }

    @Override
    public ReservationEntity convertToEntity(ReservationDto reservationDto, UserEntity personEntity, UserEntity roomEntity) {
        return mapperService.mapReservationDtoToEntity(reservationDto, personEntity, roomEntity);
    }

    @Override
    public ReservationDto convertToDto(ReservationEntity reservationEntity, String personEmail) {
        return mapperService.mapReservationEntityToDto(reservationEntity, personEmail);
    }

}
