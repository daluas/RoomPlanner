package edu.roomplanner.validation.validator.impl;

import edu.roomplanner.builders.ReservationEntityBuilder;
import edu.roomplanner.entity.PersonEntity;
import edu.roomplanner.entity.ReservationEntity;
import edu.roomplanner.entity.RoomEntity;
import edu.roomplanner.repository.ReservationRepository;
import edu.roomplanner.util.ReservationEntityUtil;
import edu.roomplanner.validation.ValidationResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class AvailabilityValidatorTest {

    @Mock
    private ReservationRepository repository;

    @InjectMocks
    private AvailabilityValidator sut;

    private ReservationEntityUtil util = new ReservationEntityUtil();

    @Test
    public void shouldFindAValidDate() {
        ValidationResult expectedResult = new ValidationResult();
        Calendar startDate = util.createDate(2007, Calendar.JANUARY, 6, 10, 10, 0);
        Calendar endDate = util.createDate(2007, Calendar.JANUARY, 6, 10, 45, 0);
        ReservationEntity reservationEntity = util.getReservationEntity(2L, startDate, endDate);
        when(repository.findNonAvailableDate(reservationEntity.getStartDate(), reservationEntity.getEndDate(), reservationEntity.getRoom().getId()))
                .thenReturn(new ArrayList<>());

        ValidationResult result = sut.validate(reservationEntity);

        assertEquals(result.getError(), expectedResult.getError());
    }

    @Test
    public void shouldNotFindAValidDate() {
        ValidationResult expectedResult = new ValidationResult("The date is not available!");
        Calendar startDate = util.createDate(2007, Calendar.JANUARY, 6, 10, 10, 0);
        Calendar endDate = util.createDate(2007, Calendar.JANUARY, 6, 10, 45, 0);
        ReservationEntity reservationEntity = util.getReservationEntity(2L, startDate, endDate);
        ReservationEntity reservationEntity2 = util.getReservationEntity(3L, startDate, endDate);
        when(repository.findNonAvailableDate(reservationEntity.getStartDate(), reservationEntity.getEndDate(), reservationEntity.getRoom().getId()))
                .thenReturn(Arrays.asList(reservationEntity2));

        ValidationResult result = sut.validate(reservationEntity);

        assertEquals(expectedResult.getError(), result.getError());
    }

    @Test
    public void shouldFindAValidDateForUserWithTheSameId() {
        ValidationResult expectedResult = new ValidationResult();
        Calendar startDate = util.createDate(2007, Calendar.JANUARY, 6, 10, 10, 0);
        Calendar endDate = util.createDate(2007, Calendar.JANUARY, 6, 10, 45, 0);
        ReservationEntity reservationEntity = util.getReservationEntity(1L, startDate, endDate);
        when(repository.findNonAvailableDate(reservationEntity.getStartDate(), reservationEntity.getEndDate(), reservationEntity.getRoom().getId()))
                .thenReturn(Arrays.asList(reservationEntity));

        ValidationResult result = sut.validate(reservationEntity);

        assertEquals(expectedResult.getError(), result.getError());
    }

    @Test
    public void shouldFindAValidDateForUserWithTheSameIdAndWithDataOverlapping() {
        ValidationResult expectedResult = new ValidationResult();
        Calendar startDate = util.createDate(2007, Calendar.JANUARY, 6, 10, 10, 0);
        Calendar endDate = util.createDate(2007, Calendar.JANUARY, 6, 10, 45, 0);
        ReservationEntity reservationEntity = util.getReservationEntity(1L, startDate, endDate);
        startDate = util.createDate(2007, Calendar.JANUARY, 6, 9, 10, 0);
        endDate = util.createDate(2007, Calendar.JANUARY, 6, 11, 45, 0);
        ReservationEntity reservationEntity2 = util.getReservationEntity(1L, startDate, endDate);
        when(repository.findNonAvailableDate(reservationEntity.getStartDate(), reservationEntity.getEndDate(), reservationEntity.getRoom().getId()))
                .thenReturn(Arrays.asList(reservationEntity2));

        ValidationResult result = sut.validate(reservationEntity);

        assertEquals(expectedResult.getError(), result.getError());
    }

    @Test
    public void shouldFindAValidDateForUserWithoutTheSameIdAndWithDataOverlapping() {
        ValidationResult expectedResult = new ValidationResult("The date is not available!");
        Calendar startDate = util.createDate(2007, Calendar.JANUARY, 6, 10, 10, 0);
        Calendar endDate = util.createDate(2007, Calendar.JANUARY, 6, 10, 45, 0);
        ReservationEntity reservationEntity = util.getReservationEntity(1L, startDate, endDate);
        startDate = util.createDate(2007, Calendar.JANUARY, 6, 9, 10, 0);
        endDate = util.createDate(2007, Calendar.JANUARY, 6, 11, 45, 0);
        ReservationEntity reservationEntity2 = util.getReservationEntity(3L, startDate, endDate);
        when(repository.findNonAvailableDate(reservationEntity.getStartDate(), reservationEntity.getEndDate(), reservationEntity.getRoom().getId()))
                .thenReturn(Arrays.asList(reservationEntity2));

        ValidationResult result = sut.validate(reservationEntity);

        assertEquals(expectedResult.getError(), result.getError());
    }

}