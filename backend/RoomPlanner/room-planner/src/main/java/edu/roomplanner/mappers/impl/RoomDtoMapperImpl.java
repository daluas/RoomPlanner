package edu.roomplanner.mappers.impl;

import edu.roomplanner.builders.UserDtoBuilder;
import edu.roomplanner.dto.ReservationDto;
import edu.roomplanner.dto.RoomDto;
import edu.roomplanner.entity.ReservationEntity;
import edu.roomplanner.entity.RoomEntity;
import edu.roomplanner.entity.UserEntity;
import edu.roomplanner.mappers.ReservationDtoMapper;
import edu.roomplanner.mappers.RoomDtoMapper;
import edu.roomplanner.repository.RoomRepository;
import edu.roomplanner.repository.UserRepository;
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


    public RoomDto mapEntityToDto(RoomEntity roomEntity) {

        Set<ReservationEntity> reservationEntitySet = roomEntity.getReservations();
        Set<ReservationDto> reservationDtoSet = mapRoomEntitySetToDtoSet(reservationEntitySet);
        return (RoomDto) UserDtoBuilder.builder()
                .withId(roomEntity.getId())
                .withEmail(roomEntity.getEmail())
                .withType(roomEntity.getType())
                .withFloor(roomEntity.getFloor())
                .withName(roomEntity.getName())
                .withMaxPersons(roomEntity.getMaxPersons())
                .withReservations(reservationDtoSet)
                .build();
    }

    public List<RoomDto> mapEntityListToDtoList(List<UserEntity> roomEntityList) {
        List<RoomDto> roomDtoList = new ArrayList<>();
        for (UserEntity room : roomEntityList) {
            roomDtoList.add(mapEntityToDto((RoomEntity) room));
        }
        return roomDtoList;
    }

    public Set<ReservationDto> mapRoomEntitySetToDtoSet(Set<ReservationEntity> reservationEntitySet){
        Set<ReservationDto> reservationDtoSet = new HashSet<>();
        for(ReservationEntity entity : reservationEntitySet){
            reservationDtoSet.add(reservationDtoMapper.mapReservationEntityToDto(entity));
        }
        return reservationDtoSet;
    }
}
