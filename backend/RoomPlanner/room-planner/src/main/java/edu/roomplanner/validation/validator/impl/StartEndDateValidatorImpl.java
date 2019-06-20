package edu.roomplanner.validation.validator.impl;

import edu.roomplanner.entity.ReservationEntity;
import edu.roomplanner.validation.ValidationResult;
import edu.roomplanner.validation.validator.BookingValidator;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

@Component
@Order(3)
public class StartEndDateValidatorImpl implements BookingValidator {

    @Override
    public ValidationResult validate(ReservationEntity reservationEntity) {
        Calendar startDate = reservationEntity.getStartDate();
        System.out.println();
        System.out.println(getMinutesInFuture(startDate));
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
        startDate.set(Calendar.SECOND, 0);
        return durationBetween(getSysDate(), startDate);
    }

    private Long getMinutesReservationTime(Calendar startDate, Calendar endDate) {
        endDate.set(Calendar.SECOND, 0);
        return durationBetween(startDate, endDate);
    }

    private Calendar getSysDate() {
        Calendar sysDate = Calendar.getInstance();
        sysDate.set(Calendar.SECOND, 0);
        return sysDate;
    }

    private Long durationBetween(Calendar startDate, Calendar endDate) {
        return TimeUnit.MINUTES.convert(endDate.getTime().getTime() - startDate.getTime().getTime(), TimeUnit.MILLISECONDS);
    }


}
