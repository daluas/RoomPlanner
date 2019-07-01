package edu.roomplanner.service;

import edu.roomplanner.dto.FloorDto;

import java.util.List;

public interface FloorService {

    List<FloorDto> getAllFloors();
    FloorDto getFloorByFloor(Integer floor);
}
