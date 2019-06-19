package edu.roomplanner.validation.validator.impl;

import edu.roomplanner.entity.ReservationEntity;
import edu.roomplanner.validation.ValidationResult;
import edu.roomplanner.validation.validator.BookingValidator;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Calendar;

@Component
public class StartEndDateValidatorImpl implements BookingValidator {

    private static final int MIN_MINUTES = 30;

    @Override
    public ValidationResult validate(ReservationEntity reservationEntity) {
        Calendar startDate = reservationEntity.getStartDate();
        if (getMinutesInFuture(startDate) < 0) {
            return new ValidationResult("Start date is in past!");
        }
        Calendar endDate = reservationEntity.getEndDate();
        if (getMinutesReservationTime(startDate, endDate) < MIN_MINUTES) {
            return new ValidationResult("End date is in past!");
        }
        return new ValidationResult();
    }

    private Long getMinutesInFuture(Calendar startDate) {
        startDate.set(Calendar.SECOND,0);
        return Duration.between(getSysDate().toInstant(), startDate.toInstant())
                .toMinutes();
    }

    private Long getMinutesReservationTime(Calendar startDate, Calendar endDate) {
        endDate.set(Calendar.SECOND,0);
        return Duration.between(startDate.toInstant(), endDate.toInstant())
                .toMinutes();
    }

    private Calendar getSysDate() {
        Calendar sysDate = Calendar.getInstance();
        sysDate.set(Calendar.SECOND,0);
        return sysDate;
    }


}
