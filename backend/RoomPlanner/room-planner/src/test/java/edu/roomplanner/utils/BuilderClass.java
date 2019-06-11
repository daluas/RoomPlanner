package edu.roomplanner.utils;

import edu.roomplanner.dto.RoomDto;

public class BuilderClass {

    public static RoomDto buildRoomDto(Long id, String name, Integer floor, Integer maxPersons){
        RoomDto testRoomDto = new RoomDto();
        testRoomDto.setId(id);
        testRoomDto.setName(name);
        testRoomDto.setFloor(floor);
        testRoomDto.setMaxPersons(maxPersons);
        return testRoomDto;
    }
}
