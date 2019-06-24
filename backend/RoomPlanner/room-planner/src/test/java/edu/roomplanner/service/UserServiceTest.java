package edu.roomplanner.service;

import edu.roomplanner.dto.ReservationDto;
import edu.roomplanner.dto.RoomDto;
import edu.roomplanner.entity.PersonEntity;
import edu.roomplanner.entity.ReservationEntity;
import edu.roomplanner.entity.RoomEntity;
import edu.roomplanner.entity.UserEntity;
import edu.roomplanner.mappers.ReservationDtoMapper;
import edu.roomplanner.mappers.RoomDtoMapper;
import edu.roomplanner.mappers.impl.ReservationDtoMapperImpl;
import edu.roomplanner.repository.RoomRepository;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.mockito.Mockito.when;


@RunWith(SpringRunner.class)
public class UserServiceTest {

    @Mock
    private UserValidator userValidator;

    @Mock
    private RoomDtoMapper roomDtoMapper;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private UserServiceImpl sut;

    private ReservationDtoMapper reservationDtoMapper = new ReservationDtoMapperImpl(userRepository, roomRepository);

    private final Long SECOND_PERSON_ID = 3L;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldReturnRoomDtoListWhenGetAllRoomsIsCalled() {
        UserEntity roomEntityOne = BuildersWrapper.buildRoomEntity(2L, "wonderland@yahoo.com", "4wonD2C%",
                null, UserType.ROOM, "Wonderland", 5, 14);
        UserEntity roomEntityTwo = BuildersWrapper.buildRoomEntity(3L, "westeros@yahoo.com", "4westAD8%",
                null, UserType.ROOM, "Westeros", 8, 20);

        List<UserEntity> RoomEntityList = Arrays.asList(roomEntityOne, roomEntityTwo);
        List<RoomDto> expectedRoomDtoList = roomDtoMapper.mapEntityListToDtoList(RoomEntityList);

        when(userRepository.findByType(UserType.ROOM)).thenReturn(RoomEntityList);
        when(roomDtoMapper.mapEntityListToDtoList(RoomEntityList)).thenReturn(expectedRoomDtoList);

        List<RoomDto> actualRoomDtoList = sut.getAllRooms();

        Assert.assertEquals(expectedRoomDtoList, actualRoomDtoList);
    }

    @Test
    public void shouldReturnExpectedRoomDtoWhenGetRoomByIdIsCalled() {
        UserEntity roomEntity = BuildersWrapper.buildRoomEntity(1L, "wonderland@yahoo.com", "4wonD2C%",
                null, UserType.ROOM, "Wonderland", 5, 14);
        Set<ReservationEntity> reservationEntitySet = buildReservationEntitySet(roomEntity);
        roomEntity.setReservations(reservationEntitySet);

        Set<ReservationDto> reservationDtoSet = buildReservationDtoSet(reservationEntitySet);
        RoomDto expectedRoomDto = BuildersWrapper.buildRoomDto(2L, "wonderland@yahoo.com", "Wonderland",
                reservationDtoSet, 5, 14, UserType.ROOM);

        when(userValidator.checkValidRoomId(1L)).thenReturn(true);
        when(roomDtoMapper.mapEntityToDto((RoomEntity) roomEntity)).thenReturn(expectedRoomDto);
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(roomEntity));
        when(userRepository.findByType(UserType.ROOM)).thenReturn(Collections.singletonList(roomEntity));

        RoomDto actualRoomDto = sut.getRoomById(1L);

        Assert.assertEquals(expectedRoomDto, actualRoomDto);
    }

    private Set<ReservationEntity> buildReservationEntitySet(UserEntity roomEntity) {
        UserEntity personEntity = BuildersWrapper.buildPersonEntity(2L, "sghitun@yahoo.com", "sghitun",
                null, UserType.PERSON, "Ghitun", "Stefania");
        ReservationEntity reservationEntity = BuildersWrapper.buildReservationEntity(1L, new GregorianCalendar(), new GregorianCalendar(),
                "reservation1", personEntity, roomEntity);
        Set<ReservationEntity> reservationEntitySet = new HashSet<>();
        UserEntity personEntityTwo = BuildersWrapper.buildPersonEntity(SECOND_PERSON_ID, personEntity.getEmail(),
                personEntity.getPassword(), personEntity.getReservations(), personEntity.getType(), null, null);
        ReservationEntity reservationEntityTwo = BuildersWrapper.buildReservationEntity(2L, new GregorianCalendar(), new GregorianCalendar(),
                "reservation2", personEntityTwo, roomEntity);
        reservationEntitySet.add(reservationEntity);
        reservationEntitySet.add(reservationEntityTwo);
        return reservationEntitySet;
    }

    private Set<ReservationDto> buildReservationDtoSet(Set<ReservationEntity> reservationEntities) {
        Set<ReservationDto> reservationDtos = new HashSet<>();
        for(ReservationEntity reservationEntity:reservationEntities) {
            if(reservationEntity.getPerson().getId().equals(SECOND_PERSON_ID)) {
                reservationEntity.setDescription(null);
            }
            ReservationDto reservationDto = reservationDtoMapper.mapReservationEntityToDto(reservationEntity);
            reservationDtos.add(reservationDto);
        }
        return reservationDtos;
    }

}
