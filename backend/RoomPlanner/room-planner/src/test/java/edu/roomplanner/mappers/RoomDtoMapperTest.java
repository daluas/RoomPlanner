package edu.roomplanner.mappers;

import edu.roomplanner.dto.RoomDto;
import edu.roomplanner.entity.FloorEntity;
import edu.roomplanner.entity.RoomEntity;
import edu.roomplanner.entity.UserEntity;
import edu.roomplanner.mappers.impl.RoomDtoMapperImpl;
import edu.roomplanner.types.UserType;
import edu.roomplanner.util.BuildersWrapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
public class RoomDtoMapperTest {

    @Mock
    private ReservationDtoMapper reservationDtoMapper;

    @InjectMocks
    private RoomDtoMapperImpl sut;

    @Test
    public void shouldReturnRoomDtoWhenMapEntityToDtoIsCalledwithValidRoomEntity() {
        UserEntity userEntity = BuildersWrapper.buildRoomEntity(2L, "wonderland@yahoo.com", "wonderland",
                new HashSet<>(), BuildersWrapper.buildFloorEntity(1L, 5), UserType.ROOM, "Wonderland", 14);
        RoomDto expectedRoomDto = BuildersWrapper.buildRoomDto(2L, "wonderland@yahoo.com", "Wonderland",
                new HashSet<>(), 5, 14, UserType.ROOM);
        RoomDto actualRoomDto = sut.mapEntityToDto((RoomEntity) userEntity);

        assertEquals(expectedRoomDto, actualRoomDto);
    }

    @Test
    public void shouldReturnEmptyRoomDtoWhenMapEntityToDtoIsCalledwithEmptyRoomEntity() {
        UserEntity userEntity = new RoomEntity();
        userEntity.setType(UserType.ROOM);
        ((RoomEntity) userEntity).setReservations(new HashSet<>());
        ((RoomEntity) userEntity).setFloor(new FloorEntity());
        RoomDto expectedRoomDto = new RoomDto();
        expectedRoomDto.setType(UserType.ROOM);
        RoomDto actualRoomDto = sut.mapEntityToDto((RoomEntity) userEntity);

        assertEquals(expectedRoomDto, actualRoomDto);
    }


    @Test
    public void shouldReturnRoomDtoListWhenMapEntityListToDtoListIsCalledwithValidRoomEntityList() {
        UserEntity roomEntityOne = BuildersWrapper.buildRoomEntity(2L, "wonderland@yahoo.com", "4wonD2C%",
                new HashSet<>(), BuildersWrapper.buildFloorEntity(1L, 5), UserType.ROOM, "Wonderland", 14);
        UserEntity roomEntityTwo = BuildersWrapper.buildRoomEntity(3L, "westeros@yahoo.com", "4westAD8%",
                new HashSet<>(), BuildersWrapper.buildFloorEntity(2L, 8), UserType.ROOM, "Westeros", 20);
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
        ((RoomEntity) roomEntityOne).setReservations(new HashSet<>());
        ((RoomEntity) roomEntityOne).setFloor(new FloorEntity());
        UserEntity roomEntityTwo = new RoomEntity();
        roomEntityTwo.setType(UserType.ROOM);
        ((RoomEntity) roomEntityTwo).setReservations(new HashSet<>());
        ((RoomEntity) roomEntityTwo).setFloor(new FloorEntity());
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
