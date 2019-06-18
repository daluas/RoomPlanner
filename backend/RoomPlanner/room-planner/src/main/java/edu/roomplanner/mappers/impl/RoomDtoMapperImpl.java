package edu.roomplanner.mappers.impl;

import edu.roomplanner.builders.RoomDtoBuilder;
import edu.roomplanner.dto.RoomDto;
import edu.roomplanner.entity.RoomEntity;
import edu.roomplanner.entity.UserEntity;
import edu.roomplanner.mappers.RoomDtoMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RoomDtoMapperImpl implements RoomDtoMapper {

    public RoomDto mapEntityToDto(RoomEntity roomEntity) {
        RoomDto roomDto = new RoomDtoBuilder()
                .withId(roomEntity.getId())
                .withFloor(roomEntity.getFloor())
                .withName(roomEntity.getName())
                .withMaxPersons(roomEntity.getMaxPersons())
                .build();
        return roomDto;
    }

    public List<RoomDto> mapEntityListToDtoList(List<UserEntity> roomEntityList) {
        List<RoomDto> roomDtoList = new ArrayList<>();
        for (UserEntity room : roomEntityList) {
            roomDtoList.add(mapEntityToDto((RoomEntity) room));
        }
        return roomDtoList;
    }
}
