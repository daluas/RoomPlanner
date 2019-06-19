package edu.roomplanner.validation.validator.impl;

import edu.roomplanner.entity.ReservationEntity;
import edu.roomplanner.entity.RoomEntity;
import edu.roomplanner.repository.ReservationRepository;
import edu.roomplanner.validation.ValidationResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class AvailabilityValidatorImplTest {

    @Mock
    private ReservationRepository repository;

    @InjectMocks
    private AvailabilityValidatorImpl sut;

    @Test
    public void shouldFindAValidDate() {
        ValidationResult expectedResult = new ValidationResult();
        ReservationEntity reservationEntity = getReservationEntityWithStartEndDate();
        when(repository.findAvailableDate(reservationEntity.getStartDate().getTime(), reservationEntity.getEndDate().getTime(), reservationEntity.getRoom().getId())).thenReturn(new ArrayList<>());

        ValidationResult result = sut.validate(reservationEntity);

        assertEquals(result.getError(), expectedResult.getError());
    }

    @Test
    public void shouldNotFindAValidDate() {
        ValidationResult expectedResult = new ValidationResult("The date is not available!");
        ReservationEntity reservationEntity = getReservationEntityWithStartEndDate();
        when(repository.findAvailableDate(reservationEntity.getStartDate().getTime(), reservationEntity.getEndDate().getTime(), reservationEntity.getRoom().getId())).thenReturn(Arrays.asList(reservationEntity));

        ValidationResult result = sut.validate(reservationEntity);

        assertEquals(result.getError(), expectedResult.getError());
    }

    private ReservationEntity getReservationEntityWithStartEndDate() {

        RoomEntity room = new RoomEntity();
        room.setId(1L);

        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        startDate.set(2007, Calendar.JANUARY, 6, 10, 10, 0);
        endDate.set(2007, Calendar.JANUARY, 6, 10, 45, 0);

        return new ReservationEntity().startDate(startDate)
                .endDate(endDate)
                .room(room);

    }


}