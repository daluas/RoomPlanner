package edu.roomplanner.service.impl;

import edu.roomplanner.dto.FloorDto;
import edu.roomplanner.mappers.FloorDtoMapper;
import edu.roomplanner.repository.FloorRepository;
import edu.roomplanner.service.FloorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FloorServiceImpl implements FloorService {

    private FloorRepository floorRepository;

    private FloorDtoMapper floorDtoMapper;

    @Autowired
    public FloorServiceImpl(FloorRepository floorRepository, FloorDtoMapper floorDtoMapper) {
        this.floorRepository = floorRepository;
        this.floorDtoMapper = floorDtoMapper;
    }

    @Override
    public List<FloorDto> getAllFloors() {

        return floorDtoMapper.mapEntityListToDtoList(floorRepository.findAll());
    }

    @Override
    public FloorDto getFloorByFloor(Integer floor) {
        return floorDtoMapper.mapEntityToDtoWithReservations(floorRepository.findByFloor(floor));
    }
}
