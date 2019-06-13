package edu.roomplanner.mappers;

import edu.roomplanner.dto.RoomDto;
import edu.roomplanner.entity.RoomEntity;
import edu.roomplanner.entity.UserEntity;

import java.util.List;

public interface RoomDtoMapper {
    RoomDto mapEntityToDto(RoomEntity roomEntity);

    List<RoomDto> mapEntityListToDtoList(List<UserEntity> roomEntityList);
}
