package edu.roomplanner.validation.validator.impl;

import edu.roomplanner.builders.ReservationEntityBuilder;
import edu.roomplanner.entity.PersonEntity;
import edu.roomplanner.entity.ReservationEntity;
import edu.roomplanner.entity.RoomEntity;
import edu.roomplanner.entity.UserEntity;
import edu.roomplanner.types.UserType;
import edu.roomplanner.validation.ValidationResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
public class RoomPersonValidatorTest {

    private RoomPersonValidator sut = new RoomPersonValidator();

    @Test
    public void shouldVerifyThatPersonWhoWantsToBookIsRoom() {

        ReservationEntity reservationEntity = getPersonAsRoom();
        ValidationResult result = sut.validate(reservationEntity);
        assertEquals("The person that want to book is a ROOM", result.getError());

    }

    @Test
    public void shouldVerifyThatRoomToBeBookedIsPerson() {
        ReservationEntity reservationEntity = getRoomAsPerson();
        ValidationResult result = sut.validate(reservationEntity);
        assertEquals("The room to book is a PERSON", result.getError());
    }

    @Test
    public void shouldVerifyThatPersonAndRoomAreFine() {
        ReservationEntity reservationEntity = getValidReservationEntity();
        ValidationResult result = sut.validate(reservationEntity);
        assertNull(result.getError());
    }

    private ReservationEntity getPersonAsRoom() {

        UserEntity room = new RoomEntity();
        room.setType(UserType.ROOM);

        return new ReservationEntityBuilder()
                .withPerson(room)
                .build();
    }

    private ReservationEntity getRoomAsPerson() {

        UserEntity person = new PersonEntity();
        person.setType(UserType.PERSON);

        return new ReservationEntityBuilder()
                .withPerson(person)
                .withRoom(person)
                .build();
    }

    private ReservationEntity getValidReservationEntity() {

        PersonEntity person = new PersonEntity();
        person.setType(UserType.PERSON);

        RoomEntity room = new RoomEntity();
        room.setType(UserType.ROOM);

        return new ReservationEntityBuilder()
                .withRoom(room)
                .withPerson(person)
                .build();
    }


}