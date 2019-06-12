package edu.roomplanner.utils;

import edu.roomplanner.dto.RoomDto;
import edu.roomplanner.entity.PersonEntity;
import edu.roomplanner.entity.RoomEntity;
import edu.roomplanner.entity.UserEntity;
import edu.roomplanner.types.UserType;

public class BuilderClass {

    public static RoomDto buildRoomDto(Long id, String name, Integer floor, Integer maxPersons) {
        RoomDto testRoomDto = new RoomDto();
        testRoomDto.setId(id);
        testRoomDto.setName(name);
        testRoomDto.setFloor(floor);
        testRoomDto.setMaxPersons(maxPersons);
        return testRoomDto;
    }

    public static UserEntity buildRoomEntity(Long id, String email, String password, UserType type, String name, Integer floor, Integer maxPersons){
        UserEntity testUserEntity = new RoomEntity();
        testUserEntity.setId(id);
        testUserEntity.setEmail(email);
        testUserEntity.setPassword(password);
        testUserEntity.setType(type);
        testUserEntity.setId(id);
        ((RoomEntity) testUserEntity).setName(name);
        ((RoomEntity) testUserEntity).setFloor(floor);
        ((RoomEntity) testUserEntity).setMaxPersons(maxPersons);

        return testUserEntity;
    }

    public static UserEntity buildPersonEntity(Long id, String email, String password, UserType type, String firstName, String lastName){
        UserEntity testUserEntity = new PersonEntity();
        testUserEntity.setId(id);
        testUserEntity.setEmail(email);
        testUserEntity.setPassword(password);
        testUserEntity.setType(type);
        testUserEntity.setId(id);
        ((PersonEntity) testUserEntity).setFirstName(firstName);
        ((PersonEntity) testUserEntity).setLastName(lastName);

        return testUserEntity;
    }
}
