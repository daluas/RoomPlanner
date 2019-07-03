package edu.roomplanner.mappers;

import edu.roomplanner.dto.FloorDto;
import edu.roomplanner.entity.FloorEntity;

import java.util.List;

public interface FloorDtoMapper {

    FloorDto mapEntityToDtoWithoutReservations(FloorEntity floorEntity);

    FloorDto mapEntityToDtoWithReservations(FloorEntity floorEntity);

    List<FloorDto> mapEntityListToDtoList(List<FloorEntity> floorEntityList);

}
