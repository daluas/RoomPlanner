package edu.roomplanner.service;

import edu.roomplanner.dto.FloorDto;
import edu.roomplanner.entity.FloorEntity;
import edu.roomplanner.mappers.impl.FloorDtoMapperImpl;
import edu.roomplanner.repository.FloorRepository;
import edu.roomplanner.service.impl.FloorServiceImpl;
import edu.roomplanner.util.BuildersWrapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
public class FloorServiceImplTest {

    @Mock
    private FloorRepository floorRepository;

    @Mock
    private FloorDtoMapperImpl floorDtoMapper;

    @InjectMocks
    private FloorServiceImpl sut;

    @Test
    public void shouldReturnFloorDtoListWhenGetAllFloorsIsCalled() {

        FloorEntity floorEntityOne = BuildersWrapper.buildFloorEntity(1L, 5);
        FloorEntity floorEntityTwo = BuildersWrapper.buildFloorEntity(2L, 8);
        FloorEntity floorEntityThree = BuildersWrapper.buildFloorEntity(3L, 4);

        List<FloorEntity> floorEntities = Arrays.asList(floorEntityOne, floorEntityTwo, floorEntityThree);
        List<FloorDto> expectedDtos = floorDtoMapper.mapEntityListToDtoList(floorEntities);

        when(floorRepository.findAll()).thenReturn(floorEntities);
        when(floorDtoMapper.mapEntityListToDtoList(floorEntities)).thenReturn(expectedDtos);

        List<FloorDto> actualList = sut.getAllFloors();

        assertEquals(actualList, expectedDtos);

    }

}