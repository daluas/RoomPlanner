package edu.roomplanner.util;

import edu.roomplanner.builders.ReservationEntityBuilder;
import edu.roomplanner.builders.UserDtoBuilder;
import edu.roomplanner.builders.UserEntityBuilder;
import edu.roomplanner.dto.ReservationDto;
import edu.roomplanner.dto.RoomDto;
import edu.roomplanner.entity.ReservationEntity;
import edu.roomplanner.entity.UserEntity;
import edu.roomplanner.types.UserType;

import java.util.Calendar;
import java.util.Set;

public class BuildersWrapper {

    public static RoomDto buildRoomDto(Long id, String email, String name, Set<ReservationDto> reservationDtos, Integer floor, Integer maxPersons, UserType type) {
        return (RoomDto) UserDtoBuilder.builder()
                .withId(id)
                .withName(name)
                .withEmail(email)
                .withReservations(reservationDtos)
                .withFloor(floor)
                .withType(type)
                .withMaxPersons(maxPersons)
                .build();
    }

    public static UserEntity buildRoomEntity(Long id, String email, String password, Set<ReservationEntity> reservations, UserType type, String name, Integer floor, Integer maxPersons) {
        return UserEntityBuilder.builder()
                .withId(id)
                .withEmail(email)
                .withPassword(password)
                .withReservations(reservations)
                .withType(type)
                .withName(name)
                .withFloor(floor)
                .withMaxPersons(maxPersons)
                .build();
    }

    public static UserEntity buildPersonEntity(Long id, String email, String password, Set<ReservationEntity> reservations, UserType type, String firstName, String lastName) {
        return UserEntityBuilder.builder()
                .withId(id)
                .withEmail(email)
                .withPassword(password)
                .withReservations(reservations)
                .withType(type)
                .withFirstName(firstName)
                .withLastName(lastName)
                .build();
    }

    public static ReservationEntity buildReservationEntity(Long id, Calendar startDate, Calendar endDate, String description, UserEntity person, UserEntity room) {
        return ReservationEntityBuilder.builder()
                .withId(id)
                .withStartDate(startDate)
                .withEndDate(endDate)
                .withDescription(description)
                .withPerson(person)
                .withRoom(room)
                .build();
    }
}
