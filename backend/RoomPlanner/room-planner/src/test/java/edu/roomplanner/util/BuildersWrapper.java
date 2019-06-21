package edu.roomplanner.util;

import edu.roomplanner.builders.PersonEntityBuilder;
import edu.roomplanner.builders.RoomDtoBuilder;
import edu.roomplanner.builders.RoomEntityBuilder;
import edu.roomplanner.dto.FloorDto;
import edu.roomplanner.dto.RoomDto;
import edu.roomplanner.entity.FloorEntity;
import edu.roomplanner.entity.UserEntity;
import edu.roomplanner.types.UserType;

public class BuildersWrapper {

    public static RoomDto buildRoomDto(Long id, String name, FloorDto floor, Integer maxPersons) {
        return new RoomDtoBuilder()
                .withId(id)
                .withName(name)
                .withFloor(floor)
                .withMaxPersons(maxPersons)
                .build();
    }

    public static UserEntity buildRoomEntity(Long id, String email, String password, UserType type, String name, FloorEntity floor, Integer maxPersons) {
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
}
