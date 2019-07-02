package edu.roomplanner.service.impl;

import edu.roomplanner.dto.FloorDto;
import edu.roomplanner.entity.FloorEntity;
import edu.roomplanner.mappers.FloorDtoMapper;
import edu.roomplanner.repository.FloorRepository;
import edu.roomplanner.service.FloorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

        Optional<FloorEntity> floorEntity = floorRepository.findByFloor(floor);

        if (floorEntity.isPresent()) {
            return floorDtoMapper.mapEntityToDtoWithReservations(floorEntity.get());
        }
        return null;
    }
}
