package edu.roomplanner.mappers;

import edu.roomplanner.dto.RoomDto;
import edu.roomplanner.entity.RoomEntity;
import edu.roomplanner.entity.UserEntity;
import edu.roomplanner.mappers.impl.RoomDtoMapperImpl;
import edu.roomplanner.types.UserType;
import edu.roomplanner.util.BuildersWrapper;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class RoomDtoMapperTest {

    private RoomDtoMapperImpl sut = new RoomDtoMapperImpl();

    @Test
    public void shouldReturnRoomDtoWhenMapEntityToDtoIsCalledwithValidRoomEntity() {
        UserEntity userEntity = BuildersWrapper.buildRoomEntity(1L, "wonderland@yahoo.com", "4wonD2C%",
                UserType.ROOM, "Wonderland", 5, 14);
        RoomDto expectedRoomDto = BuildersWrapper.buildRoomDto(1L, "Wonderland", 5, 14);
        RoomDto actualRoomDto = sut.mapEntityToDto((RoomEntity) userEntity);

        assertEquals(expectedRoomDto, actualRoomDto);
    }

    @Test
    public void shouldReturnEmptyRoomDtoWhenMapEntityToDtoIsCalledwithEmptyRoomEntity() {
        UserEntity userEntity = new RoomEntity();
        RoomDto expectedRoomDto = new RoomDto();
        RoomDto actualRoomDto = sut.mapEntityToDto((RoomEntity) userEntity);

        assertEquals(expectedRoomDto, actualRoomDto);
    }


    @Test
    public void shouldReturnRoomDtoListWhenMapEntityListToDtoListIsCalledwithValidRoomEntityList() {
        UserEntity roomEntityOne = BuildersWrapper.buildRoomEntity(1L, "wonderland@yahoo.com", "4wonD2C%",
                UserType.ROOM, "Wonderland", 5, 14);
        UserEntity roomEntityTwo = BuildersWrapper.buildRoomEntity(2L, "westeros@yahoo.com", "4westAD8%",
                UserType.ROOM, "Westeros", 8, 21);
        List<UserEntity> roomEntityList = Arrays.asList(roomEntityOne, roomEntityTwo);

        List<RoomDto> actualRoomDtoList = sut.mapEntityListToDtoList(roomEntityList);

        RoomDto roomDtoOne = BuildersWrapper.buildRoomDto(1L, "Wonderland", 5, 14);
        RoomDto roomDtoTwo = BuildersWrapper.buildRoomDto(2L, "Westeros", 8, 21);
        List<RoomDto> expectedRoomDtoList = Arrays.asList(roomDtoOne, roomDtoTwo);

        assertEquals(expectedRoomDtoList, actualRoomDtoList);
    }

    @Test
    public void shouldReturnEmptyObjectRoomDtoListWhenMapEntityListToDtoListIsCalledwithEmptyRoomEntityList() {
        UserEntity roomEntityOne = new RoomEntity();
        UserEntity roomEntityTwo = new RoomEntity();
        List<UserEntity> roomEntityList = Arrays.asList(roomEntityOne, roomEntityTwo);

        List<RoomDto> actualRoomDtoList = sut.mapEntityListToDtoList(roomEntityList);

        RoomDto roomDtoOne = new RoomDto();
        RoomDto roomDtoTwo = new RoomDto();
        List<RoomDto> expectedRoomDtoList = Arrays.asList(roomDtoOne, roomDtoTwo);

        assertEquals(expectedRoomDtoList, actualRoomDtoList);
    }

}
