package edu.roomplanner.service.impl;

import edu.roomplanner.dto.ReservationDto;
import edu.roomplanner.entity.ReservationEntity;
import edu.roomplanner.exception.InvalidReservationDtoException;
import edu.roomplanner.exception.InvalidReservationException;
import edu.roomplanner.exception.UserNotFoundException;
import edu.roomplanner.mappers.ReservationDtoMapper;
import edu.roomplanner.repository.ReservationRepository;
import edu.roomplanner.service.ReservationService;
import edu.roomplanner.validation.BookingChain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Service
public class ReservationServiceImpl implements ReservationService {

    private ReservationDtoMapper mapperService;
    private ReservationRepository reservationRepository;
    private BookingChain bookingChain;

    @Autowired
    public ReservationServiceImpl(ReservationDtoMapper mapperService, ReservationRepository reservationRepository,
                                  BookingChain bookingChain) {
        this.mapperService = mapperService;
        this.reservationRepository = reservationRepository;
        this.bookingChain = bookingChain;
    }

    @Override
    public ReservationDto createReservation(Long roomId, ReservationDto reservationDto) {
        reservationDto.setRoomId(roomId);

        if (areReservationDtoMembersNull(reservationDto)) {
            throw new InvalidReservationDtoException("One or more members are null");
        }

        ReservationEntity reservationEntity = convertToEntity(reservationDto);

        List<String> reservationValidatorErrors = bookingChain.validate(reservationEntity);

        if (!reservationValidatorErrors.isEmpty()) {
            throw createInvalidReservationException(reservationValidatorErrors);
        }

        reservationRepository.save(reservationEntity);
        ReservationDto currentReservationDto = convertToDto(reservationEntity);
        currentReservationDto.setRoomId(roomId);

        return currentReservationDto;
    }


    @Override
    public ReservationEntity convertToEntity(ReservationDto reservationDto) {
        ReservationEntity reservationEntity = mapperService.mapReservationDtoToEntity(reservationDto);
        if (reservationEntity.getPerson() == null) {
            throw new UserNotFoundException("Invalid email");
        }
        if (reservationEntity.getRoom() == null) {
            throw new UserNotFoundException("Invalid room id");
        }
        return reservationEntity;
    }

    @Override
    public ReservationDto convertToDto(ReservationEntity reservationEntity) {
        return mapperService.mapReservationEntityToDto(reservationEntity);
    }

    private Boolean areReservationDtoMembersNull(ReservationDto reservationDto) {
        return Stream.of(reservationDto.getEndDate(), reservationDto.getStartDate(),
                reservationDto.getDescription(), reservationDto.getEmail())
                .anyMatch(Objects::isNull);
    }

    private InvalidReservationException createInvalidReservationException(List<String> reservationValidatorErrors) {
        StringBuilder validatorErrors = new StringBuilder();
        for (String error : reservationValidatorErrors) {
            validatorErrors.append(error);
            validatorErrors.append(" && ");
        }
        return new InvalidReservationException(validatorErrors.toString().substring(0, validatorErrors.length() - 4));
    }

}
