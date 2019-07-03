package edu.roomplanner.service.impl;

import edu.roomplanner.builders.ReservationEntityBuilder;
import edu.roomplanner.entity.PersonEntity;
import edu.roomplanner.entity.ReservationEntity;
import edu.roomplanner.repository.UserRepository;
import edu.roomplanner.service.PrevalidationService;
import edu.roomplanner.types.UserType;
import edu.roomplanner.validation.ValidationResult;
import edu.roomplanner.validation.validator.impl.AvailabilityValidator;
import edu.roomplanner.validation.validator.impl.StartEndDateValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public HttpStatus prevalidate(Calendar startDate, Calendar endDate, String email, Long roomId) {
        if (verifyAllParameters(startDate, endDate, email, roomId)) {
            return HttpStatus.BAD_REQUEST;
        }

        PersonEntity personEntity = (PersonEntity) userRepository.findByEmail(email).get();
        ReservationEntity reservationEntity = getReservationEntity(startDate, endDate, personEntity, roomId);

        ValidationResult validStartEndDate = startEndDateValidator.validate(reservationEntity);
        if (validStartEndDate.getError() != null) {
            return HttpStatus.BAD_REQUEST;
        }

        ValidationResult validDate = availabilityValidator.validate(reservationEntity);
        if (validDate.getError() == null) {
            return HttpStatus.OK;
        }
        return HttpStatus.BAD_REQUEST;
    }

    private ReservationEntity getReservationEntity(Calendar startDate, Calendar endDate, PersonEntity personEntity, Long roomId) {
        return new ReservationEntityBuilder()
                .withStartDate(startDate)
                .withEndDate(endDate)
                .withRoom(userRepository.findById(roomId).get())
                .withPerson(personEntity)
                .build();
    }

    private boolean verifyPersonEntity(String email) {
        return userRepository.findByEmail(email)
                .filter(userEntity -> userEntity.getType().equals(UserType.PERSON))
                .isPresent();
    }

    private boolean verifyRoomEntity(Long roomId) {
        return userRepository.findById(roomId)
                .filter((userEntity -> userEntity.getType().equals(UserType.ROOM)))
                .isPresent();
    }

    private boolean verifyAllParameters(Calendar startDate, Calendar endDate, String email, Long roomId) {
        return verifyParameters(startDate, endDate, email, roomId) || !verifyPersonEntity(email) || !verifyRoomEntity(roomId);
    }

    private boolean verifyParameters(Calendar startDate, Calendar endDate, String email, Long roomId) {
        return startDate == null || endDate == null || email == null || roomId == null;
    }

}
