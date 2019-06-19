package edu.roomplanner.validation.validator.impl;

import edu.roomplanner.builders.ReservationEntityBuilder;
import edu.roomplanner.entity.ReservationEntity;
import edu.roomplanner.validation.ValidationResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Calendar;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
public class StartEndDateValidatorImplUnitTest {

    private StartEndDateValidatorImpl sut = new StartEndDateValidatorImpl();
    private Calendar sysDate = Calendar.getInstance();
    private final int MIN_MINUTES = 30;

    @Test
    public void shouldVerifyThatStartDateIsInPast() {
        ReservationEntity reservationEntity = createReservationEntityWithStartDate(2007, 8, 12, 10, 12);
        ValidationResult result = sut.validate(reservationEntity);
        assertEquals("Start date is in past!", result.getError());
    }

    @Test
    public void shouldVerifyThatStartDateIsInPastMinutes() {
        ReservationEntity reservationEntity = createReservationEntityWithStartDate(sysDate.get(Calendar.YEAR), sysDate.get(Calendar.MONTH), sysDate.get(Calendar.DAY_OF_MONTH),
                sysDate.get(Calendar.HOUR_OF_DAY), sysDate.get(Calendar.MINUTE) - 1);
        ValidationResult result = sut.validate(reservationEntity);
        assertEquals("Start date is in past!", result.getError());
    }

    @Test
    public void shouldVerifyThatEndDateIsInPast() {
        ReservationEntity reservationEntity = createReservationEntityWithEndDate(2019, 5, 14, 11, 20);
        ValidationResult result = sut.validate(reservationEntity);
        assertEquals("End date is in past!", result.getError());
    }

    @Test
    public void shouldVerifyThatEndDateNotVerifyMinMinutes() {
        ReservationEntity reservationEntity = createReservationEntityWithEndDate(sysDate.get(Calendar.YEAR), sysDate.get(Calendar.MONTH), sysDate.get(Calendar.DAY_OF_MONTH)
                , sysDate.get(Calendar.HOUR_OF_DAY), sysDate.get(Calendar.MINUTE) + MIN_MINUTES - 1);
        ValidationResult result = sut.validate(reservationEntity);
        assertEquals("End date is in past!", result.getError());
    }

    @Test
    public void shouldVerifyThatNearEndDateIsFine() {
        ReservationEntity reservationEntity = createReservationEntityWithEndDate(sysDate.get(Calendar.YEAR), sysDate.get(Calendar.MONTH), sysDate.get(Calendar.DAY_OF_MONTH)
                , sysDate.get(Calendar.HOUR_OF_DAY), sysDate.get(Calendar.MINUTE) + MIN_MINUTES + 1);
        ValidationResult result = sut.validate(reservationEntity);
        assertNull(result.getError());
    }

    @Test
    public void shouldVerifyFutureDate() {
        ReservationEntity reservationEntity = createReservationEntityWithEndDate(sysDate.get(Calendar.YEAR) + 10, sysDate.get(Calendar.MONTH), sysDate.get(Calendar.DAY_OF_MONTH)
                , sysDate.get(Calendar.HOUR_OF_DAY), sysDate.get(Calendar.MINUTE) + MIN_MINUTES);
        ValidationResult result = sut.validate(reservationEntity);
        assertNull(result.getError());
    }


    private ReservationEntity createReservationEntityWithStartDate(int startYear, int startMonth, int startDay, int startHour, int startMinute) {

        Calendar startDate = Calendar.getInstance();
        startDate.set(startYear, startMonth, startDay, startHour, startMinute, 0);

        return new ReservationEntityBuilder()
                .withStartDate(startDate)
                .build();
    }

    private ReservationEntity createReservationEntityWithEndDate(int endYear, int endMonth, int endDay, int endHour, int endMinute) {

        Calendar endDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        startDate.set(sysDate.get(Calendar.YEAR), sysDate.get(Calendar.MONTH), sysDate.get(Calendar.DAY_OF_MONTH)
                , sysDate.get(Calendar.HOUR_OF_DAY), sysDate.get(Calendar.MINUTE), 0);
        endDate.set(endYear, endMonth, endDay, endHour, endMinute, 0);

        return new ReservationEntityBuilder()
                .withStartDate(startDate)
                .withEndDate(endDate)
                .build();
    }

}