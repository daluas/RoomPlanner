package edu.roomplanner.validation.validator.impl;

import edu.roomplanner.builders.ReservationEntityBuilder;
import edu.roomplanner.entity.PersonEntity;
import edu.roomplanner.entity.ReservationEntity;
import edu.roomplanner.entity.RoomEntity;
import edu.roomplanner.repository.UserRepository;
import edu.roomplanner.validation.ValidationResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.mockito.Mockito.when;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
public class RoomPersonIdValidatorTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private RoomPersonIdValidator sut;

    @Test
    public void shouldNotFindAValidUser() {

        ReservationEntity reservation = getReservationEntityOnlyPerson();

        when(userRepository.findById(reservation.getPerson().getId()))
                .thenReturn(Optional.empty());

        ValidationResult expectedResult = new ValidationResult("User is missing");
        ValidationResult actualResult = sut.validate(reservation);

        assertEquals(expectedResult.getError(), actualResult.getError());
    }

    @Test
    public void shouldNotFindValidRoom() {

        ReservationEntity reservation = getReservationEntity();

        when(userRepository.findById(reservation.getPerson().getId()))
                .thenReturn(Optional.of(new PersonEntity()));

        when(userRepository.findById(reservation.getRoom().getId()))
                .thenReturn(Optional.empty());

        ValidationResult expectedResult = new ValidationResult("Room is missing");
        ValidationResult actualResult = sut.validate(reservation);

        assertEquals(expectedResult.getError(), actualResult.getError());
    }

    private ReservationEntity getReservationEntityOnlyPerson() {

        PersonEntity person = new PersonEntity();
        person.setId(2L);

        return new ReservationEntityBuilder()
                .withPerson(person)
                .build();
    }

    private ReservationEntity getReservationEntity() {

        RoomEntity room = new RoomEntity();
        room.setId(2L);

        return new ReservationEntityBuilder()
                .withPerson(new PersonEntity())
                .withRoom(room)
                .build();
    }


}