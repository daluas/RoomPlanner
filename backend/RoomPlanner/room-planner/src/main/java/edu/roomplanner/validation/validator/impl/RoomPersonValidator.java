package edu.roomplanner.validation.validator.impl;

import edu.roomplanner.entity.ReservationEntity;
import edu.roomplanner.types.UserType;
import edu.roomplanner.validation.ValidationResult;
import edu.roomplanner.validation.validator.BookingValidator;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(2)
public class RoomPersonValidator implements BookingValidator {

    @Override
    public ValidationResult validate(ReservationEntity reservationEntity) {
        if (isPersonRoom(reservationEntity)) {
            return new ValidationResult("The person that want to book is a ROOM");
        }
        if (isRoomPerson(reservationEntity)) {
            return new ValidationResult("The room to book is a PERSON");
        }
        return new ValidationResult();
    }

    private boolean isPersonRoom(ReservationEntity reservationEntity) {
        return reservationEntity.getPerson()
                .getType()
                .equals(UserType.ROOM);
    }

    private boolean isRoomPerson(ReservationEntity reservationEntity) {
        return reservationEntity.getRoom()
                .getType()
                .equals(UserType.PERSON);
    }

}
