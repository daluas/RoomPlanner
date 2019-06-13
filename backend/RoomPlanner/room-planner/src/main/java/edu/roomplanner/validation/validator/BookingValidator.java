package edu.roomplanner.validation.validator;

import edu.roomplanner.entity.ReservationEntity;
import edu.roomplanner.validation.ValidationResult;

public interface BookingValidator {
    ValidationResult validate (ReservationEntity reservationEntity);
}
