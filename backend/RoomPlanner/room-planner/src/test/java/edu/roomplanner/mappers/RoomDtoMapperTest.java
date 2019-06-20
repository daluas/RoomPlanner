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
        RoomDto expectedRoomDto = BuildersWrapper.buildRoomDto(2L, "wonderland@yahoo.com", "Wonderland", null, 5, 14, UserType.ROOM);
        RoomDto actualRoomDto = sut.mapEntityToDto((RoomEntity) userEntity);

        assertEquals(expectedRoomDto, actualRoomDto);
    }

    @Test
    public void shouldReturnEmptyRoomDtoWhenMapEntityToDtoIsCalledwithEmptyRoomEntity() {
        UserEntity userEntity = new RoomEntity();
        userEntity.setType(UserType.ROOM);
        RoomDto expectedRoomDto = new RoomDto();
        expectedRoomDto.setType(UserType.ROOM);
        RoomDto actualRoomDto = sut.mapEntityToDto((RoomEntity) userEntity);

        assertEquals(expectedRoomDto, actualRoomDto);
    }


    @Test
    public void shouldReturnRoomDtoListWhenMapEntityListToDtoListIsCalledwithValidRoomEntityList() {
        UserEntity roomEntityOne = BuildersWrapper.buildRoomEntity(1L, "wonderland@yahoo.com", "4wonD2C%",
                UserType.ROOM, "Wonderland", 5, 14);
        UserEntity roomEntityTwo = BuildersWrapper.buildRoomEntity(2L, "westeros@yahoo.com", "4westAD8%",
                UserType.ROOM, "Westeros", 8, 20);
        List<UserEntity> roomEntityList = Arrays.asList(roomEntityOne, roomEntityTwo);

        List<RoomDto> actualRoomDtoList = sut.mapEntityListToDtoList(roomEntityList);

        RoomDto roomDtoOne = BuildersWrapper.buildRoomDto(2L, "wonderland@yahoo.com", "Wonderland", null, 5, 14, UserType.ROOM);
        RoomDto roomDtoTwo = BuildersWrapper.buildRoomDto(3L, "westeros@yahoo.com", "Westeros", null, 8, 20, UserType.ROOM);
        List<RoomDto> expectedRoomDtoList = Arrays.asList(roomDtoOne, roomDtoTwo);

        assertEquals(expectedRoomDtoList, actualRoomDtoList);
    }

    @Test
    public void shouldReturnEmptyObjectRoomDtoListWhenMapEntityListToDtoListIsCalledwithEmptyRoomEntityList() {
        UserEntity roomEntityOne = new RoomEntity();
        roomEntityOne.setType(UserType.ROOM);
        UserEntity roomEntityTwo = new RoomEntity();
        roomEntityTwo.setType(UserType.ROOM);
        List<UserEntity> roomEntityList = Arrays.asList(roomEntityOne, roomEntityTwo);

        List<RoomDto> actualRoomDtoList = sut.mapEntityListToDtoList(roomEntityList);

        RoomDto roomDtoOne = new RoomDto();
        roomDtoOne.setType(UserType.ROOM);
        RoomDto roomDtoTwo = new RoomDto();
        roomDtoTwo.setType(UserType.ROOM);
        List<RoomDto> expectedRoomDtoList = Arrays.asList(roomDtoOne, roomDtoTwo);

        assertEquals(expectedRoomDtoList, actualRoomDtoList);
    }

}
