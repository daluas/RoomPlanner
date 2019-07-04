package edu.roomplanner.service.impl;

import edu.roomplanner.builders.ReservationEntityBuilder;
import edu.roomplanner.entity.PersonEntity;
import edu.roomplanner.entity.ReservationEntity;
import edu.roomplanner.entity.RoomEntity;
import edu.roomplanner.entity.UserEntity;
import edu.roomplanner.exception.*;
import edu.roomplanner.repository.UserRepository;
import edu.roomplanner.service.PrevalidationService;
import edu.roomplanner.types.UserType;
import edu.roomplanner.validation.ValidationResult;
import edu.roomplanner.validation.validator.impl.AvailabilityValidator;
import edu.roomplanner.validation.validator.impl.StartEndDateValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;

@Service
public class PrevalidationServiceImpl implements PrevalidationService {

    private StartEndDateValidator startEndDateValidator;

    private AvailabilityValidator availabilityValidator;

    private UserRepository userRepository;

    @Autowired
    public PrevalidationServiceImpl(AvailabilityValidator availabilityValidator, UserRepository userRepository, StartEndDateValidator startEndDateValidator) {
        this.availabilityValidator = availabilityValidator;
        this.userRepository = userRepository;
        this.startEndDateValidator = startEndDateValidator;
    }

    @Override
    public void prevalidate(Calendar startDate, Calendar endDate, String email, Long roomId) {
        verifyParametersValidation(startDate, endDate, email, roomId);

        UserEntity personEntity = getPersonEntity(email);

        if(!isUserPerson(personEntity)) {
            throw new UserAuthorityException("Not a person");
        }

        UserEntity roomEntity = getRoomEntity(roomId);

        if(!isUserRoom(roomEntity)) {
            throw new UserAuthorityException("Not a room");
        }

        ReservationEntity reservationEntity = getReservationEntity(startDate, endDate,
                                              (PersonEntity) personEntity, (RoomEntity) roomEntity);

        ValidationResult validStartEndDate = startEndDateValidator.validate(reservationEntity);
        if (validStartEndDate.getError() != null) {
            throw new InvalidDateException(validStartEndDate.getError());
        }

        ValidationResult validDate = availabilityValidator.validate(reservationEntity);
        if (validDate.getError() != null) {
            throw new InvalidDateException(validStartEndDate.getError());
        }
    }

    private ReservationEntity getReservationEntity(Calendar startDate, Calendar endDate, PersonEntity personEntity, RoomEntity roomEntity) {
        return ReservationEntityBuilder.builder()
                .withStartDate(startDate)
                .withEndDate(endDate)
                .withRoom(roomEntity)
                .withPerson(personEntity)
                .build();
    }

    private UserEntity getPersonEntity(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found! Invalid email"));
    }

    private UserEntity getRoomEntity(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RoomNotFoundException("Room not found! Invalid id"));
    }

    private boolean isUserPerson(UserEntity personEntity) {
        return personEntity.getType().equals(UserType.PERSON);
    }

    private boolean isUserRoom(UserEntity roomEntity) {
        return roomEntity.getType().equals(UserType.ROOM);
    }

    private void verifyParametersValidation(Calendar startDate, Calendar endDate,
                                            String email, Long roomId) {
        if(areParametersNotNull(startDate, endDate, email, roomId)) {
            throw new InvalidReservationDtoException("Invalid reservation dto");
        }
    }

    private boolean areParametersNotNull(Calendar startDate, Calendar endDate, String email, Long roomId) {
        return startDate == null || endDate == null || email == null || roomId == null;
    }

}
