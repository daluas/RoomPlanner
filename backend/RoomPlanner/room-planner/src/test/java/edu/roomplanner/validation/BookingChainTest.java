package edu.roomplanner.validation;

import edu.roomplanner.entity.ReservationEntity;
import edu.roomplanner.validation.validator.BookingValidator;
import edu.roomplanner.validation.validator.impl.AvailabilityValidatorImpl;
import edu.roomplanner.validation.validator.impl.RoomPersonIdValidatorImpl;
import edu.roomplanner.validation.validator.impl.RoomPersonValidatorImpl;
import edu.roomplanner.validation.validator.impl.StartEndDateValidatorImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
public class BookingChainTest {

    @Spy
    private List<BookingValidator> validators = new ArrayList<>();

    @Mock
    private AvailabilityValidatorImpl availabilityValidator;

    @Mock
    private RoomPersonIdValidatorImpl roomPersonIdValidator;

    @Mock
    private RoomPersonValidatorImpl roomPersonValidator;

    @Mock
    private StartEndDateValidatorImpl startEndDateValidator;


    @InjectMocks
    private BookingChain chain;


    @Before
    public void setup() {
        validators.add(availabilityValidator);
        validators.add(roomPersonIdValidator);
        validators.add(roomPersonValidator);
        validators.add(startEndDateValidator);
    }


    @Test
    public void shouldSeeIfValidatorsAreCalled() {
        ReservationEntity reservationEntity = new ReservationEntity();

        when(availabilityValidator.validate(reservationEntity)).thenReturn(new ValidationResult("Error"));
        when(roomPersonIdValidator.validate(reservationEntity)).thenReturn(new ValidationResult("Error"));
        when(roomPersonValidator.validate(reservationEntity)).thenReturn(new ValidationResult("Error"));
        when(startEndDateValidator.validate(reservationEntity)).thenReturn(new ValidationResult("Error"));

        List<String> errors = chain.validate(reservationEntity);

        assertEquals(errors.size(), 4);

    }


}