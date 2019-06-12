package edu.roomplanner.service.impl;

import edu.roomplanner.dto.RoomDto;
import edu.roomplanner.entity.RoomEntity;
import edu.roomplanner.entity.UserEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MapperServiceImpl {

    public RoomDto mapToDto(RoomEntity roomEntity) {
        RoomDto roomDto = new RoomDto();
        roomDto.setId(roomEntity.getId());
        roomDto.setFloor(roomEntity.getFloor());
        roomDto.setName(roomEntity.getName());
        roomDto.setMaxPersons(roomEntity.getMaxPersons());
        return roomDto;
    }

    public List<RoomDto> mapListToDto(List<UserEntity> roomEntityList) {
        List<RoomDto> roomDtoList = new ArrayList<>();
        for (UserEntity room : roomEntityList) {
            roomDtoList.add(mapToDto((RoomEntity) room));
        }
        return roomDtoList;
    }
}
