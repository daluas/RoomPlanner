package edu.roomplanner.util;


import edu.roomplanner.builders.FloorDtoBuilder;
import edu.roomplanner.builders.FloorEntityBuilder;
import edu.roomplanner.builders.UserDtoBuilder;
import edu.roomplanner.builders.UserEntityBuilder;
import edu.roomplanner.dto.FloorDto;
import edu.roomplanner.dto.ReservationDto;
import edu.roomplanner.dto.RoomDto;
import edu.roomplanner.entity.FloorEntity;
import edu.roomplanner.entity.RoomEntity;
import edu.roomplanner.entity.UserEntity;
import edu.roomplanner.types.UserType;

import java.util.Set;

public class BuildersWrapper {


    public static RoomDto buildRoomDto(Long id, String email, String name, Set<ReservationDto> reservationDtos, FloorDto floor, Integer maxPersons, UserType type) {
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

    public static UserEntity buildRoomEntity(Long id, String email, String password, UserType type, String name, FloorEntity floor, Integer maxPersons) {
        return UserEntityBuilder.builder()
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
        return UserEntityBuilder.builder()
                .withId(id)
                .withEmail(email)
                .withPassword(password)
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


}
