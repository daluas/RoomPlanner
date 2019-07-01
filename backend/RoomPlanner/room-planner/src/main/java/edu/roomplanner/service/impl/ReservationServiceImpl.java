package edu.roomplanner.service.impl;

import edu.roomplanner.dto.ReservationDto;
import edu.roomplanner.entity.ReservationEntity;
import edu.roomplanner.entity.UserEntity;
import edu.roomplanner.mappers.ReservationDtoMapper;
import edu.roomplanner.repository.ReservationRepository;
import edu.roomplanner.repository.UserRepository;
import edu.roomplanner.service.ReservationService;
import edu.roomplanner.service.TokenParserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class ReservationServiceImpl implements ReservationService {

    private ReservationDtoMapper mapperService;
    private ReservationRepository reservationRepository;
    private TokenParserService tokenParserService;
    private UserRepository userRepository;

    @Autowired
    public ReservationServiceImpl(ReservationDtoMapper mapperService, ReservationRepository reservationRepository, TokenParserService tokenParserService, UserRepository userRepository) {
        this.mapperService = mapperService;
        this.reservationRepository = reservationRepository;
        this.tokenParserService = tokenParserService;
        this.userRepository = userRepository;
    }

    @Override
    public Optional<ReservationDto> createReservation(Long roomId, ReservationDto reservationDto) {
        reservationDto.setRoomId(roomId);

        if (areReservationDtoMembersNull(reservationDto)) {
            return Optional.empty();
        }

        ReservationEntity reservationEntity = convertToEntity(reservationDto);
        reservationRepository.save(reservationEntity);
        ReservationDto currentReservationDto = convertToDto(reservationEntity);
        currentReservationDto.setRoomId(roomId);

        return Optional.of(currentReservationDto);
    }


    @Override
    public ReservationEntity convertToEntity(ReservationDto reservationDto) {
        ReservationEntity reservationEntity = mapperService.mapReservationDtoToEntity(reservationDto);
        if (reservationEntity.getPerson() == null) {
            throw new UsernameNotFoundException("Invalid email");
        }
        if (reservationEntity.getRoom() == null) {
            throw new UsernameNotFoundException("Invalid room id");
        }
        return reservationEntity;
    }

    @Override
    public ReservationDto convertToDto(ReservationEntity reservationEntity) {
        return mapperService.mapReservationEntityToDto(reservationEntity);
    }

    @Override
    public HttpStatus deleteReservation(Long reservationId) {
        if (!reservationRepository.findById(reservationId).isPresent()) {
            return HttpStatus.NOT_FOUND;
        }
        String userEmail = tokenParserService.getEmailFromToken();
        UserEntity userEntity = userRepository.findByEmail(userEmail).get();
        Long userId = userEntity.getId();
        Long userInReservation = reservationRepository.findById(reservationId).get()
                .getPerson()
                .getId();

        if (!userId.equals(userInReservation)) {
            return HttpStatus.NOT_FOUND;
        }
        reservationRepository.deleteById(reservationId);
        return HttpStatus.OK;

    }

    private Boolean areReservationDtoMembersNull(ReservationDto reservationDto) {
        return Stream.of(reservationDto.getEndDate(), reservationDto.getStartDate(),
                reservationDto.getDescription(), reservationDto.getEmail())
                .anyMatch(Objects::isNull);
    }

}
