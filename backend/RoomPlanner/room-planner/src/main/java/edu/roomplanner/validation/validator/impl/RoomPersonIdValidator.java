package edu.roomplanner.validation.validator.impl;

import edu.roomplanner.entity.ReservationEntity;
import edu.roomplanner.repository.UserRepository;
import edu.roomplanner.validation.ValidationResult;
import edu.roomplanner.validation.validator.BookingValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class RoomPersonIdValidator implements BookingValidator {

    private UserRepository userRepository;

    @Autowired
    public RoomPersonIdValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ValidationResult validate(ReservationEntity reservationEntity) {
        if (!isPersonInDB(reservationEntity)) {
            return new ValidationResult("User is missing");
        }
        if (!isRoomInDB(reservationEntity)) {
            return new ValidationResult("Room is missing");
        }
        return new ValidationResult();
    }

    private boolean isPersonInDB(ReservationEntity reservationEntity) {
        return userRepository.findById(reservationEntity.getPerson()
                .getId())
                .isPresent();
    }

    private boolean isRoomInDB(ReservationEntity reservationEntity) {
        return userRepository.findById(reservationEntity.getRoom()
                .getId())
                .isPresent();
    }
}
