package edu.roomplanner.validation.validator.impl;

import edu.roomplanner.entity.ReservationEntity;
import edu.roomplanner.validation.ValidationResult;
import edu.roomplanner.validation.validator.BookingValidator;

import java.time.Duration;
import java.util.Calendar;

public class StartEndDateValidatorImpl implements BookingValidator {

    @Override
    public ValidationResult validate(ReservationEntity reservationEntity){
        Calendar startDate = reservationEntity.getStartDate();
      //
        long daysInPast = Duration.between(startDate.toInstant(),Calendar.getInstance().toInstant()).toDays();
        if(daysInPast < 0){
            return new ValidationResult("Start date is in past!");
        }
        Calendar endDate = reservationEntity.getEndDate();




    }

}
