package edu.roomplanner.mappers;

import edu.roomplanner.dto.FloorDto;
import edu.roomplanner.entity.FloorEntity;
import edu.roomplanner.mappers.impl.FloorDtoMapperImpl;
import edu.roomplanner.util.BuildersWrapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;


public class FloorDtoMapperImplTest {

    @Autowired
    private FloorDtoMapperImpl sut;

    @Test
    public void shouldReturnFloorDtoListWhenMapEntityToDtoIsCalledWithValidFloorEntityList() {
        FloorEntity floorEntityOne = BuildersWrapper.buildFloorEntity(1L, 5);
        FloorEntity floorEntityTwo = BuildersWrapper.buildFloorEntity(2L, 8);
        FloorEntity floorEntityThree = BuildersWrapper.buildFloorEntity(3L, 4);

        List<FloorEntity> floorEntityList = Arrays.asList(floorEntityOne, floorEntityTwo, floorEntityThree);

        List<FloorDto> actualFloorDtoList = sut.mapEntityListToDtoList(floorEntityList);

        FloorDto floorDtoOne = BuildersWrapper.buildFloorDto(1L, 5);
        FloorDto floorDtoTwo = BuildersWrapper.buildFloorDto(2L, 8);
        FloorDto floorDtoThree = BuildersWrapper.buildFloorDto(3L, 4);

        List<FloorDto> expectedFloorDtoList = Arrays.asList(floorDtoOne, floorDtoTwo, floorDtoThree);

        assertEquals(expectedFloorDtoList, actualFloorDtoList);
    }

    @Test
    public void shouldReturnEmptyFloorDtoWhenMapEntityToDtoIsCalledWithEmptyFloorEntity() {

        FloorEntity floorEntity = new FloorEntity();
        FloorDto expectedFloorDto = new FloorDto();
        FloorDto actualFloorDto = sut.mapEntityToDto(floorEntity);

        assertEquals(expectedFloorDto, actualFloorDto);
    }

    @Test
    public void shouldReturnFloorDtoWhenMapEntityToDtoIsCalledWithValidFloorEntity() {
        FloorEntity floorEntity = BuildersWrapper.buildFloorEntity(1L, 5);
        FloorDto expectedFloorDto = BuildersWrapper.buildFloorDto(1L, 5);
        FloorDto actualFloorDto = sut.mapEntityToDto(floorEntity);

        assertEquals(expectedFloorDto, actualFloorDto);
    }

    @Test
    public void shouldReturnEmptyObjectFloorDtoListWhenMapEntityListToDtoListIsCalledWithEmptyFloorEntityList() {
        FloorEntity floorEntityOne = new FloorEntity();
        FloorEntity floorEntityTwo = new FloorEntity();
        FloorEntity floorEntityThree = new FloorEntity();

        List<FloorEntity> floorEntityList = Arrays.asList(floorEntityOne, floorEntityTwo, floorEntityThree);

        List<FloorDto> actualFloorDtoList = sut.mapEntityListToDtoList(floorEntityList);

        FloorDto floorDtoOne = new FloorDto();
        FloorDto floorDtoTwo = new FloorDto();
        FloorDto floorDtoThree = new FloorDto();

        List<FloorDto> expectedFloorDtoList = Arrays.asList(floorDtoOne, floorDtoTwo, floorDtoThree);

        assertEquals(expectedFloorDtoList, actualFloorDtoList);

    }

}
