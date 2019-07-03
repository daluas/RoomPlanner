package edu.roomplanner.service.impl;

import edu.roomplanner.builders.ReservationDtoBuilder;
import edu.roomplanner.builders.ReservationEntityBuilder;
import edu.roomplanner.dto.ReservationDto;
import edu.roomplanner.entity.ReservationEntity;
import edu.roomplanner.exception.*;
import edu.roomplanner.mappers.ReservationDtoMapper;
import edu.roomplanner.repository.ReservationRepository;
import edu.roomplanner.repository.UserRepository;
import edu.roomplanner.entity.UserEntity;
import edu.roomplanner.exception.InvalidReservationDtoException;
import edu.roomplanner.exception.InvalidReservationException;
import edu.roomplanner.exception.UserNotFoundException;
import edu.roomplanner.service.ReservationService;
import edu.roomplanner.service.TokenParserService;
import edu.roomplanner.types.UserType;
import edu.roomplanner.util.ConverterUtil;
import edu.roomplanner.validation.BookingChain;
import edu.roomplanner.validation.ValidationResult;
import edu.roomplanner.validation.validator.impl.StartEndDateValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Stream;

@Service
public class ReservationServiceImpl implements ReservationService {

    private ReservationDtoMapper mapperService;
    private ReservationRepository reservationRepository;
    private BookingChain bookingChain;
    private StartEndDateValidator startEndDateValidator;
    private TokenParserService tokenParserService;
    private UserRepository userRepository;

    @Autowired
    public ReservationServiceImpl(ReservationDtoMapper mapperService, ReservationRepository reservationRepository,
                                  BookingChain bookingChain, StartEndDateValidator startEndDateValidator, TokenParserService tokenParserService,
                                  UserRepository userRepository) {
        this.mapperService = mapperService;
        this.reservationRepository = reservationRepository;
        this.bookingChain = bookingChain;
        this.startEndDateValidator = startEndDateValidator;
        this.tokenParserService = tokenParserService;
        this.userRepository = userRepository;
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

    private ReservationEntity convertToEntity(ReservationDto reservationDto) {
        ReservationEntity reservationEntity = mapperService.mapReservationDtoToEntity(reservationDto);
        if (reservationEntity.getPerson() == null) {
            throw new UserNotFoundException("Invalid email");
        }
        if (reservationEntity.getRoom() == null) {
            throw new UserNotFoundException("Invalid room id");
        }
        return reservationEntity;
    }

    private ReservationDto convertToDto(ReservationEntity reservationEntity) {
        return mapperService.mapReservationEntityToDto(reservationEntity);
    }

    @Override
    public void deleteReservation(Long reservationId) {
        if (!reservationRepository.findById(reservationId).isPresent()) {
            throw new ReservationNotFoundException("Invalid reservation ID");
        }
        String userEmail = tokenParserService.getEmailFromToken();
        UserEntity userEntity = userRepository.findByEmail(userEmail).
                orElseThrow(() -> new UserNotFoundException("User not found! Bad email"));
        Long userId = userEntity.getId();
        Long userInReservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException("Reservation not found! ID not found"))
                .getPerson()
                .getId();

        if (!userId.equals(userInReservation)) {
            throw new UnauthorizedReservationException("Can't delete another user reservation");
        }
        reservationRepository.deleteById(reservationId);

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

    @Override
    public ReservationDto updateReservation(Long id, ReservationDto reservationDto) {
        ReservationDto updateDto = createUpdateReservationDto(reservationDto);

        String userEmail = tokenParserService.getEmailFromToken();
        verifyParameters(userEmail, id);

        ReservationEntity reservationEntity = reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException("Reservation not found! ID not found"));

        ValidationResult result = startEndDateValidator.validate(getReservation(updateDto));

        if (result.getError() != null) {
            throw new InvalidDateException(result.getError());
        }

        verifyPersonId(reservationEntity, userEmail);
        return getReservationDtoUpdated(reservationEntity, reservationDto);
    }
    
    private ReservationDto createUpdateReservationDto(ReservationDto reservationDto) {
        ReservationDto copyDto = ReservationDtoBuilder.builder()
                .withEndDate(Calendar.getInstance())
                .withStartDate(Calendar.getInstance())
                .build();
        copyDto.getStartDate().setTime((Date) reservationDto.getStartDate().getTime().clone());
        copyDto.getEndDate().setTime((Date) reservationDto.getEndDate().getTime().clone());

        copyDto.getStartDate().setTime(ConverterUtil.conversionToGmt(reservationDto.getStartDate().getTime()));
        copyDto.getEndDate().setTime(ConverterUtil.conversionToGmt(reservationDto.getEndDate().getTime()));
        
        return copyDto;
    }

    private ReservationDto getReservationDtoUpdated(ReservationEntity reservationEntity, ReservationDto reservationDto) {
        reservationEntity.setStartDate(reservationDto.getStartDate());
        reservationEntity.setEndDate(reservationDto.getEndDate());
        reservationEntity.setDescription(reservationDto.getDescription());
        ReservationEntity updatedEntity = reservationRepository.save(reservationEntity);
        return mapperService.mapReservationEntityToDto(updatedEntity);
    }

    private void verifyPersonId(ReservationEntity reservationEntity, String userEmail) {
        Long userId = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found! Email not found!"))
                .getId();
        if (!userId.equals(reservationEntity.getPerson().getId())) {
            throw new PersonIdOfReservationNotEqualsUserIdException("Person id of reservation is not the same as user id.");
        }
    }

    private boolean existsReservation(Long id) {
        return reservationRepository.findById(id)
                .isPresent();
    }

    private boolean isEmailOwnerPerson(String userEmail) {
        return userRepository.findByEmail(userEmail)
                .filter(user -> user.getType().equals(UserType.PERSON))
                .isPresent();
    }

    private ReservationEntity getReservation(ReservationDto reservationDto) {
        Calendar startDate = reservationDto.getStartDate();
        Calendar endDate = reservationDto.getEndDate();
        return ReservationEntityBuilder.builder()
                .withStartDate(startDate)
                .withEndDate(endDate)
                .build();
    }

    private void verifyParameters(String userEmail, Long id) {
        if (!existsReservation(id)) {
            throw new ReservationNotFoundException("Reservation not found! Bad id");
        }
        if (!isEmailOwnerPerson(userEmail)) {
            throw new NotPersonException("You are not a person");
        }
    }
    
}
