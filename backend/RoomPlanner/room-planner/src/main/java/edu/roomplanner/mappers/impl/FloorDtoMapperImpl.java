package edu.roomplanner.mappers.impl;

import edu.roomplanner.builders.FloorDtoBuilder;
import edu.roomplanner.dto.FloorDto;
import edu.roomplanner.dto.RoomDto;
import edu.roomplanner.entity.FloorEntity;
import edu.roomplanner.entity.RoomEntity;
import edu.roomplanner.mappers.FloorDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class FloorDtoMapperImpl implements FloorDtoMapper {

    private RoomDtoMapperImpl roomMapper;

    @Autowired
    public FloorDtoMapperImpl(RoomDtoMapperImpl roomMapper) {
        this.roomMapper = roomMapper;
    }

    @Override
    public FloorDto mapEntityToDtoWithoutReservations(FloorEntity floorEntity) {

        Set<RoomDto> roomsDto = mapRoomEntityListToRoomDtoListWithoutReservations(floorEntity.getRooms());

        return FloorDtoBuilder.builder()
                .withId(floorEntity.getId())
                .withFloor(floorEntity.getFloor())
                .withRooms(roomsDto)
                .build();
    }

    @Override
    public FloorDto mapEntityToDtoWithReservations(FloorEntity floorEntity) {

        if (floorEntity == null) {
            return new FloorDto();
        }

        Set<RoomDto> roomsDto = mapRoomEntityListToRoomDtoListWithReservations(floorEntity.getRooms());

        return FloorDtoBuilder.builder()
                .withId(floorEntity.getId())
                .withFloor(floorEntity.getFloor())
                .withRooms(roomsDto)
                .build();
    }

    @Override
    public List<FloorDto> mapEntityListToDtoList(List<FloorEntity> roomEntityList) {
        List<FloorDto> floorsDto = new ArrayList<>();
        for (FloorEntity floorEntity : roomEntityList) {
            floorsDto.add(mapEntityToDtoWithoutReservations(floorEntity));
        }
        return floorsDto;

    }

    private Set<RoomDto> mapRoomEntityListToRoomDtoListWithoutReservations(Set<RoomEntity> rooms) {
        return Optional.ofNullable(rooms)
                .map(this::processRoomDtoStreamWithoutReservations)
                .orElse(null);
    }

    private Set<RoomDto> processRoomDtoStreamWithoutReservations(Set<RoomEntity> rooms) {
        return rooms.stream()
                .map(roomMapper::mapEntityToDto)
                .peek((room) -> room.setReservations(new HashSet<>()))
                .collect(Collectors.toSet());

    }

    private Set<RoomDto> mapRoomEntityListToRoomDtoListWithReservations(Set<RoomEntity> rooms) {
        return Optional.ofNullable(rooms)
                .map(this::processRoomDtoStreamWithReservations)
                .orElse(null);
    }

    private Set<RoomDto> processRoomDtoStreamWithReservations(Set<RoomEntity> rooms) {
        return rooms.stream()
                .map(roomMapper::mapEntityToDto)
                .collect(Collectors.toSet());

    }

}
