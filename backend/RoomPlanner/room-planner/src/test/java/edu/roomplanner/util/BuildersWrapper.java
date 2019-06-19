package edu.roomplanner.util;

import edu.roomplanner.builders.UserEntityBuilder;
import edu.roomplanner.builders.UserDtoBuilder;
import edu.roomplanner.dto.RoomDto;
import edu.roomplanner.entity.UserEntity;
import edu.roomplanner.types.UserType;

public class BuildersWrapper {

    public static RoomDto buildRoomDto(Long id, String name, Integer floor, Integer maxPersons, UserType type) {
        return (RoomDto) UserDtoBuilder.builder()
                .withId(id)
                .withName(name)
                .withFloor(floor)
                .withType(type)
                .withMaxPersons(maxPersons)
                .build();
    }

    public static UserEntity buildRoomEntity(Long id, String email, String password, UserType type, String name, Integer floor, Integer maxPersons) {
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
}
