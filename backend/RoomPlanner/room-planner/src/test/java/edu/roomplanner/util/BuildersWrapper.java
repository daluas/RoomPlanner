package edu.roomplanner.util;

import edu.roomplanner.builders.*;
import edu.roomplanner.dto.ReservationDto;
import edu.roomplanner.dto.RoomDto;
import edu.roomplanner.entity.ReservationEntity;
import edu.roomplanner.entity.UserEntity;
import edu.roomplanner.types.UserType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class BuildersWrapper {

    public static RoomDto buildRoomDto(Long id, String name, Integer floor, Integer maxPersons) {
        return new RoomDtoBuilder()
                .withId(id)
                .withName(name)
                .withFloor(floor)
                .withMaxPersons(maxPersons)
                .build();
    }

    public static UserEntity buildRoomEntity(Long id, String email, String password, UserType type, String name, Integer floor, Integer maxPersons) {
        return new RoomEntityBuilder()
                .withId(id)
                .withEmail(email)
                .withPassword(password)
                .withType(type)
                .withName(name)
                .withFloor(floor)
                .withMaxPersons(maxPersons)
                .build();
    }

    public static UserEntity buildPersonEntiy(Long id, String email, String password, UserType type, String firstName, String lastName) {
        return new PersonEntityBuilder()
                .withId(id)
                .withEmail(email)
                .withPassword(password)
                .withType(type)
                .withFirstName(firstName)
                .withLastName(lastName)
                .build();
    }
    public static ReservationDto buildReservationDto(Long id, Long roomId, String email,Calendar startDate, Calendar endDate, String description){
        return new ReservationDtoBuilder()
                .withId(id)
                .withRoomId(roomId)
                .withEmail(email)
                .withStartDate(startDate)
                .withEndDate(endDate)
                .withDescription(description)
                .build();
    }
    public static ReservationEntity buildReservationEntity(Long id,Calendar startDate,Calendar endDate,UserEntity person,UserEntity room,String description){
        return new ReservationEntityBuilder()
                .withId(id)
                .withStartDate(startDate)
                .withEndDate(endDate)
                .withPerson(person)
                .withRoom(room)
                .withDescription(description)
                .build();
    }


}
