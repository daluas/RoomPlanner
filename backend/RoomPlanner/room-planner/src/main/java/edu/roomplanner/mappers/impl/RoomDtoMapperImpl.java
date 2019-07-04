package edu.roomplanner.mappers.impl;

import edu.roomplanner.builders.UserDtoBuilder;
import edu.roomplanner.dto.ReservationDto;
import edu.roomplanner.dto.RoomDto;
import edu.roomplanner.entity.ReservationEntity;
import edu.roomplanner.entity.RoomEntity;
import edu.roomplanner.entity.UserEntity;
import edu.roomplanner.mappers.ReservationDtoMapper;
import edu.roomplanner.mappers.RoomDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class RoomDtoMapperImpl implements RoomDtoMapper {

    @Autowired
    private ReservationDtoMapper reservationDtoMapper;

    @Autowired
    public RoomDtoMapperImpl(ReservationDtoMapper reservationDtoMapper) {
        this.reservationDtoMapper = reservationDtoMapper;
    }

    @Override
    public List<RoomDto> mapEntityListToDtoList(List<UserEntity> roomEntityList) {
        List<RoomDto> roomDtoList = new ArrayList<>();
        for (UserEntity room : roomEntityList) {
            roomDtoList.add(mapEntityToDto((RoomEntity) room));
        }
        return roomDtoList;
    }

    @Override
    public RoomDto mapEntityToDto(RoomEntity roomEntity) {

        Set<ReservationEntity> reservationEntitySet = roomEntity.getReservations();

        if (roomEntity.getFloor() == null) {
            return (RoomDto) UserDtoBuilder.builder()
                    .withType(roomEntity.getType())
                    .build();
        }

        Set<ReservationDto> reservationDtoSet = mapRoomEntitySetToDtoSet(reservationEntitySet);

        if (roomEntity.getFloor() == null) {
            return (RoomDto) UserDtoBuilder.builder()
                    .withType(roomEntity.getType())
                    .build();
        }

        return (RoomDto) UserDtoBuilder.builder()
                .withId(roomEntity.getId())
                .withEmail(roomEntity.getEmail())
                .withType(roomEntity.getType())
                .withFloor(roomEntity.getFloor().getFloor())
                .withName(roomEntity.getName())
                .withMaxPersons(roomEntity.getMaxPersons())
                .withReservations(reservationDtoSet)
                .build();
    }

    private Set<ReservationDto> mapRoomEntitySetToDtoSet(Set<ReservationEntity> reservationEntitySet) {
        Set<ReservationDto> reservationDtoSet = new HashSet<>();
        if (reservationEntitySet == null) {
            return reservationDtoSet;
        }
        for (ReservationEntity entity : reservationEntitySet) {
            reservationDtoSet.add(reservationDtoMapper.mapReservationEntityToDto(entity));
        }
        return reservationDtoSet;
    }


}
