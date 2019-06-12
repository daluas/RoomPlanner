package edu.roomplanner.service;

import edu.roomplanner.dto.RoomDto;
import edu.roomplanner.entity.RoomEntity;
import edu.roomplanner.entity.UserEntity;

import java.util.List;

public interface MapperService {

    RoomDto mapToDto(RoomEntity roomEntity);
    List<RoomDto> mapListToDto(List<UserEntity> roomEntityList);
}
