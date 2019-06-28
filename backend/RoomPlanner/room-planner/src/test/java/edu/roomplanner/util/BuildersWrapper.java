package edu.roomplanner.util;

import edu.roomplanner.builders.*;
import edu.roomplanner.dto.FloorDto;
import edu.roomplanner.dto.ReservationDto;
import edu.roomplanner.dto.RoomDto;
import edu.roomplanner.entity.FloorEntity;
import edu.roomplanner.entity.ReservationEntity;
import edu.roomplanner.entity.RoomEntity;
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

    public static UserEntity buildRoomEntity(Long id, String email, String password, Set<ReservationEntity> reservations, FloorEntity floor, UserType type, String name, Integer maxPersons) {
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

    public static FloorEntity buildFloorEntity(Long id, Integer floor) {
        return FloorEntityBuilder.builder()
                .withId(id)
                .withFloor(floor)
                .build();

    }

    public static FloorDto buildFloorDto(Long id, Integer floor) {
        return FloorDtoBuilder.builder()
                .withId(id)
                .withFloor(floor)
                .build();
    }

    public static FloorEntity buildFloorEntityWithRooms(Long id, Integer floor, Set<RoomEntity> rooms) {
        return FloorEntityBuilder.builder()
                .withId(id)
                .withFloor(floor)
                .withRooms(rooms)
                .build();
    }

    public static FloorDto buildFloorDtoWithRooms(Long id, Integer floor, Set<RoomDto> rooms) {
        return FloorDtoBuilder.builder()
                .withId(id)
                .withFloor(floor)
                .withRooms(rooms)
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

    public static ReservationDto buildReservationDto(Long id, Long roomId, String email, Calendar startDate, Calendar endDate, String description) {
        return new ReservationDtoBuilder()
                .withId(id)
                .withRoomId(roomId)
                .withEmail(email)
                .withStartDate(startDate)
                .withEndDate(endDate)
                .withDescription(description)
                .build();
    }

    public static ReservationEntity buildReservationEntity(Long id, Calendar startDate, Calendar endDate, UserEntity person, UserEntity room, String description) {
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
