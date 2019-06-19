package edu.roomplanner.validation.validator.impl;

import edu.roomplanner.entity.ReservationEntity;
import edu.roomplanner.entity.UserEntity;
import edu.roomplanner.repository.ReservationRepository;
import edu.roomplanner.validation.ValidationResult;
import edu.roomplanner.validation.validator.BookingValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.List;

@Component("Validator")
@Order(4)
public class AvailabilityValidatorImpl implements BookingValidator {

    private ReservationRepository reservationRepository;

    @Autowired
    public AvailabilityValidatorImpl(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public ValidationResult validate(ReservationEntity reservationEntity) {
        Calendar startDate = reservationEntity.getStartDate();
        Calendar endDate = reservationEntity.getEndDate();
        UserEntity room = reservationEntity.getRoom();

        List<ReservationEntity> otherReservations = reservationRepository.findAvailableDate(startDate.getTime(), endDate.getTime(), room.getId());
        if (otherReservations.isEmpty()) {
            return new ValidationResult();
        }
        return new ValidationResult("The date is not available!");

    }
}
