package edu.roomplanner.mappers;

import edu.roomplanner.dto.RoomDto;
import edu.roomplanner.entity.RoomEntity;
import edu.roomplanner.entity.UserEntity;

import java.util.ArrayList;
import java.util.List;

public class RoomDtoMapper {

    public static RoomDto mapEntityToDto(RoomEntity roomEntity) {
        RoomDto roomDto = new RoomDto();
        roomDto.setId(roomEntity.getId());
        roomDto.setFloor(roomEntity.getFloor());
        roomDto.setName(roomEntity.getName());
        roomDto.setMaxPersons(roomEntity.getMaxPersons());
        return roomDto;
    }

    public static List<RoomDto> mapEntityListToDtoList(List<UserEntity> roomEntityList) {
        List<RoomDto> roomDtoList = new ArrayList<>();
        for (UserEntity room : roomEntityList) {
            roomDtoList.add(mapEntityToDto((RoomEntity) room));
        }
        return roomDtoList;
    }
}
