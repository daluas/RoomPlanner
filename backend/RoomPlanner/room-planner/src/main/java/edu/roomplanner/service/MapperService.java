package edu.roomplanner.service;

import edu.roomplanner.dto.RoomDto;
import edu.roomplanner.entity.RoomEntity;
import edu.roomplanner.entity.UserEntity;

import java.util.List;

public interface MapperService {

    RoomDto mapEntityToDto(RoomEntity roomEntity);
    List<RoomDto> mapEntityListToDtoList(List<UserEntity> roomEntityList);
}
