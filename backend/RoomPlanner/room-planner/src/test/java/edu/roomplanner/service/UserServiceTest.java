package edu.roomplanner.service;

import edu.roomplanner.dto.RoomDto;
import edu.roomplanner.entity.FloorEntity;
import edu.roomplanner.entity.ReservationEntity;
import edu.roomplanner.entity.RoomEntity;
import edu.roomplanner.entity.UserEntity;
import edu.roomplanner.mappers.RoomDtoMapper;
import edu.roomplanner.repository.UserRepository;
import edu.roomplanner.service.impl.UserServiceImpl;
import edu.roomplanner.types.UserType;
import edu.roomplanner.util.BuildersWrapper;
import edu.roomplanner.validation.validator.UserValidator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;


@RunWith(SpringRunner.class)
public class UserServiceTest {

    @Mock
    private UserValidator userValidator;

    @Mock
    private RoomDtoMapper roomDtoMapper;

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserServiceImpl sut;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldReturnRoomDtoListWhenGetAllRoomsIsCalled() {

        UserEntity roomEntityOne = BuildersWrapper.buildRoomEntity(2L, "wonderland@yahoo.com", "4wonD2C%",
                UserType.ROOM, "Wonderland", BuildersWrapper.buildFloorEntity(1L, 5), 14);
        UserEntity roomEntityTwo = BuildersWrapper.buildRoomEntity(3L, "westeros@yahoo.com", "4westAD8%",
                UserType.ROOM, "Westeros", BuildersWrapper.buildFloorEntity(1L, 5), 20);

        List<UserEntity> RoomEntityList = Arrays.asList(roomEntityOne, roomEntityTwo);
        List<RoomDto> expectedRoomDtoList = roomDtoMapper.mapEntityListToDtoList(RoomEntityList);

        when(userRepository.findByType(UserType.ROOM)).thenReturn(RoomEntityList);
        when(roomDtoMapper.mapEntityListToDtoList(RoomEntityList)).thenReturn(expectedRoomDtoList);

        List<RoomDto> actualRoomDtoList = sut.getAllRooms();

        Assert.assertEquals(expectedRoomDtoList, actualRoomDtoList);
    }

    @Test
    public void shouldReturnExpectedRoomDtoWhenGetRoomByIdIsCalled() {

        RoomDto expectedRoomDto = BuildersWrapper.buildRoomDto(2L, "wonderland@yahoo.com", "Wonderland", null, 5, 14, UserType.ROOM);
        UserEntity userEntity = BuildersWrapper.buildRoomEntity(1L, "wonderland@yahoo.com", "4wonD2C%",
                UserType.ROOM, "Wonderland", BuildersWrapper.buildFloorEntity(1L, 5), 14);

        when(userValidator.checkValidRoomId(1L)).thenReturn(true);
        when(roomDtoMapper.mapEntityToDto((RoomEntity) userEntity)).thenReturn(expectedRoomDto);
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(userEntity));
        when(userRepository.findByType(UserType.ROOM)).thenReturn(Collections.singletonList(userEntity));

        RoomDto actualRoomDto = sut.getRoomById(1L);

        Assert.assertEquals(expectedRoomDto, actualRoomDto);
    }

    @Test
    public void shouldReturnRoomListWithReservationsInTimePeriodWhenGetRoomByFiltersIsCalledWithPastDates(){

        FloorEntity roomEntityFloor = BuildersWrapper.buildFloorEntity(1L,5);

        UserEntity userEntityRoom = BuildersWrapper.buildRoomEntity(2L, "wonderland@yahoo.com", "wonderland",
                UserType.ROOM, "Wonderland", roomEntityFloor, 14);
        List<UserEntity> userEntityList = Arrays.asList(userEntityRoom);

        RoomDto roomDto = BuildersWrapper.buildRoomDto(2L, "wonderland@yahoo.com", "Wonderland",  Collections.EMPTY_SET, 5, 14, UserType.ROOM);
        List<RoomDto> expectedRoomDtoList = Arrays.asList(roomDto);

         Calendar startDate = Calendar.getInstance();
        startDate.set(2019,Calendar.JUNE,22,10,0,0);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2019,Calendar.JUNE,22,11,30,0);

        when(userRepository.viewByFields(startDate,endDate)).thenReturn(userEntityList);
        when(roomDtoMapper.mapEntityListToDtoList(userEntityList)).thenReturn(expectedRoomDtoList);

        List<RoomDto> actualRoomDtoList = sut.getRoomsByFilters(startDate,endDate,null,null);

        Assert.assertEquals(expectedRoomDtoList, actualRoomDtoList);

    }

    @Test
    public void shouldReturnRoomListWithNoReservationsInTimePeriodWhenGetRoomByFiltersIsCalledWithPresentOrFutureDates(){

        FloorEntity roomEntityFloor = BuildersWrapper.buildFloorEntity(1L,5);

        UserEntity userEntityRoom = BuildersWrapper.buildRoomEntity(2L, "wonderland@yahoo.com", "wonderland",
                UserType.ROOM, "Wonderland", roomEntityFloor, 14);
        List<UserEntity> userEntityList = Arrays.asList(userEntityRoom);

        RoomDto roomDto = BuildersWrapper.buildRoomDto(2L, "wonderland@yahoo.com", "Wonderland",  Collections.EMPTY_SET, 5, 14, UserType.ROOM);
        List<RoomDto> expectedRoomDtoList = Arrays.asList(roomDto);

        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MINUTE,10);
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MINUTE,10);

        when(userRepository.filterByFields(startDate,endDate,null,null)).thenReturn(userEntityList);
        when(roomDtoMapper.mapEntityListToDtoList(userEntityList)).thenReturn(expectedRoomDtoList);

        List<RoomDto> actualRoomDtoList = sut.getRoomsByFilters(startDate,endDate,null,null);

        Assert.assertEquals(expectedRoomDtoList, actualRoomDtoList);
    }

    @Test
    public void shouldReturnRoomListWithNoDuplicatesWhenGetRoomByFiltersIsCalledWithPresentOrFutureDatesAndRepositoryReturnsDuplicateData(){

        FloorEntity roomEntityFloorOne = BuildersWrapper.buildFloorEntity(1L,5);
        FloorEntity roomEntityFloorTwo = BuildersWrapper.buildFloorEntity(2L,8);
        UserEntity userEntityRoom = BuildersWrapper.buildRoomEntity(2L, "wonderland@yahoo.com", "wonderland",
                UserType.ROOM, "Wonderland", roomEntityFloorOne, 14);
        UserEntity userEntityRoomUnique = BuildersWrapper.buildRoomEntity(3L, "westeros@yahoo.com", "westeros",
                UserType.ROOM, "Westeros", roomEntityFloorTwo, 20);
        List<UserEntity> userEntityList = Arrays.asList(userEntityRoom, userEntityRoom, userEntityRoom, userEntityRoomUnique);

        RoomDto roomDtoOne = BuildersWrapper.buildRoomDto(2L, "wonderland@yahoo.com", "Wonderland",  Collections.EMPTY_SET, 5, 14, UserType.ROOM);
        RoomDto roomDtoTwo = BuildersWrapper.buildRoomDto(3L, "westeros@yahoo.com", "Westeros",  Collections.EMPTY_SET, 8, 20, UserType.ROOM);
        List<RoomDto> expectedRoomDtoList = Arrays.asList(roomDtoOne,roomDtoTwo);

        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MINUTE,10);
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MINUTE,10);

        List<UserEntity> filteredUserEntityList = Arrays.asList(userEntityRoom,userEntityRoomUnique);

        when(userRepository.filterByFields(startDate,endDate,null,null)).thenReturn(userEntityList);
        when(roomDtoMapper.mapEntityListToDtoList(filteredUserEntityList)).thenReturn(expectedRoomDtoList);

        List<RoomDto> actualRoomDtoList = sut.getRoomsByFilters(startDate,endDate,null,null);

        Assert.assertEquals(expectedRoomDtoList, actualRoomDtoList);
    }
}
