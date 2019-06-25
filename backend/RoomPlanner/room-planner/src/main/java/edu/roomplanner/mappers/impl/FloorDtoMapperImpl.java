package edu.roomplanner.mappers.impl;

import edu.roomplanner.builders.FloorDtoBuilder;
import edu.roomplanner.dto.FloorDto;
import edu.roomplanner.dto.RoomDto;
import edu.roomplanner.entity.FloorEntity;
import edu.roomplanner.entity.RoomEntity;
import edu.roomplanner.mappers.FloorDtoMapper;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class FloorDtoMapperImpl implements FloorDtoMapper {

    @Override
    public FloorDto mapEntityToDto(FloorEntity floorEntity) {

        Set<RoomDto> roomsDto = mapRoomEntityListToRoomDtoList(floorEntity.getRooms());

        return new FloorDtoBuilder()
                .withId(floorEntity.getId())
                .withFloor(floorEntity.getFloor())
                .withRooms(roomsDto)
                .build();
    }

    @Override
    public List<FloorDto> mapEntityListToDtoList(List<FloorEntity> roomEntityList) {
        List<FloorDto> floorsDto = new ArrayList<>();
        for (FloorEntity floorEntity : roomEntityList) {
            floorsDto.add(mapEntityToDto(floorEntity));
        }
        return floorsDto;

    }

    private Set<RoomDto> mapRoomEntityListToRoomDtoList(Set<RoomEntity> rooms) {
        return Optional.ofNullable(rooms)
                .map(this::processRoomDtoStream)
                .orElse(null);
    }

    private Set<RoomDto> processRoomDtoStream(Set<RoomEntity> rooms) {
        RoomDtoMapperImpl roomMapper = new RoomDtoMapperImpl();
        return rooms.stream()
                .map(roomMapper::mapEntityToDto)
                .collect(Collectors.toSet());

    }

}
