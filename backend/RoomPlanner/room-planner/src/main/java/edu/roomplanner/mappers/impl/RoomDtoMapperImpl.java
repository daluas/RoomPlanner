package edu.roomplanner.mappers.impl;

import edu.roomplanner.builders.FloorDtoBuilder;
import edu.roomplanner.dto.FloorDto;
import edu.roomplanner.builders.UserDtoBuilder;
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

        if (roomEntity.getFloor() == null) {
            return (RoomDto) UserDtoBuilder.builder()
                    .withType(roomEntity.getType())
                    .build();
        }

        FloorDto floorDto = FloorDtoBuilder.builder()
                .withId(roomEntity.getFloor().getId())
                .withFloor(roomEntity.getFloor().getFloor())
                .build();

        return (RoomDto) UserDtoBuilder.builder()
                .withId(roomEntity.getId())
                .withEmail(roomEntity.getEmail())
                .withType(roomEntity.getType())
                .withFloor(floorDto)
                .withName(roomEntity.getName())
                .withMaxPersons(roomEntity.getMaxPersons())
                .build();
    }

    public List<RoomDto> mapEntityListToDtoList(List<UserEntity> roomEntityList) {
        List<RoomDto> roomDtoList = new ArrayList<>();
        for (UserEntity room : roomEntityList) {
            roomDtoList.add(mapEntityToDto((RoomEntity) room));
        }
        return roomDtoList;
    }
}
