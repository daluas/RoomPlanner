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
    private TokenParserService tokenParserService;

    @InjectMocks
    private UserServiceImpl sut;

    private ReservationDtoMapper reservationDtoMapper = new ReservationDtoMapperImpl(userRepository);

    private final Long SECOND_PERSON_ID = 3L;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldReturnRoomDtoListWhenGetAllRoomsIsCalled() {

        RoomEntity roomEntityOne = (RoomEntity) BuildersWrapper.buildRoomEntity(1L, "wonderland@yahoo.com", "4wonD2C%",
                null, BuildersWrapper.buildFloorEntity(1L, 5), UserType.ROOM, "Wonderland", 14);
        Set<ReservationEntity> reservationEntitySet = buildReservationEntitySet(roomEntityOne);
        roomEntityOne.setReservations(reservationEntitySet);
        UserEntity roomEntityTwo = BuildersWrapper.buildRoomEntity(3L, "westeros@yahoo.com", "4westAD8%",
                reservationEntitySet, BuildersWrapper.buildFloorEntity(1L, 5), UserType.ROOM, "Westeros", 20);


        List<UserEntity> roomEntityList = Arrays.asList(roomEntityOne, roomEntityTwo);

        RoomDto roomDtoOne = BuildersWrapper.buildRoomDto(1L, "wonderland@yahoo.com", "Wonderland",
                buildReservationDtoSet(reservationEntitySet), 5, 14, UserType.ROOM);
        RoomDto roomDtoTwo = BuildersWrapper.buildRoomDto(3L, "wonderland@yahoo.com", "Wonderland",
                buildReservationDtoSet(reservationEntitySet), 5, 14, UserType.ROOM);

        PersonEntity personEntity = (PersonEntity) BuildersWrapper.buildPersonEntity(2L, "sghitun@yahoo.com", "sghitun",
                null, UserType.PERSON, "Ghitun", "Stefania");

        List<RoomDto> expectedRoomDtoList = Arrays.asList(roomDtoOne, roomDtoTwo);

        when(userRepository.findByType(UserType.ROOM)).thenReturn(roomEntityList);
        when(roomDtoMapper.mapEntityListToDtoList(roomEntityList)).thenReturn(expectedRoomDtoList);
        when(tokenParserService.getEmailFromToken()).thenReturn("sghitun@yahoo.com");
        when(userRepository.findByEmail("sghitun@yahoo.com")).thenReturn(Optional.of(personEntity));

        List<RoomDto> actualRoomDtoList = sut.getAllRooms();

        Assert.assertEquals(expectedRoomDtoList, actualRoomDtoList);
    }

    @Test
    public void shouldReturnExpectedRoomDtoWhenGetRoomByIdIsCalled() {
        RoomEntity roomEntity = (RoomEntity) BuildersWrapper.buildRoomEntity(1L, "wonderland@yahoo.com", "4wonD2C%",
                null, BuildersWrapper.buildFloorEntity(1L, 5), UserType.ROOM, "Wonderland", 14);
        Set<ReservationEntity> reservationEntitySet = buildReservationEntitySet(roomEntity);
        roomEntity.setReservations(reservationEntitySet);

        RoomDto expectedRoomDto = BuildersWrapper.buildRoomDto(2L, "wonderland@yahoo.com", "Wonderland", null, 5, 14, UserType.ROOM);

        PersonEntity personEntity = (PersonEntity) BuildersWrapper.buildPersonEntity(2L, "sghitun@yahoo.com", "sghitun",
                null, UserType.PERSON, "Ghitun", "Stefania");
        when(userValidator.checkValidRoomId(1L)).thenReturn(true);
        when(roomDtoMapper.mapEntityToDto(roomEntity)).thenReturn(expectedRoomDto);
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(roomEntity));
        when(userRepository.findByType(UserType.ROOM)).thenReturn(Collections.singletonList(roomEntity));
        when(tokenParserService.getEmailFromToken()).thenReturn("sghitun@yahoo.com");
        when(userRepository.findByEmail("sghitun@yahoo.com")).thenReturn(Optional.of(personEntity));

        RoomDto actualRoomDto = sut.getRoomById(1L);

        Assert.assertEquals(expectedRoomDto, actualRoomDto);
    }

    @Test
    public void shouldReturnRoomListWithReservationsInTimePeriodWhenGetRoomByFiltersIsCalledWithPastDates() {

        RoomEntity userEntityRoom = (RoomEntity) BuildersWrapper.buildRoomEntity(1L, "wonderland@yahoo.com", "4wonD2C%",
                null, BuildersWrapper.buildFloorEntity(1L, 5), UserType.ROOM, "Wonderland", 14);
        List<UserEntity> userEntityList = Collections.singletonList(userEntityRoom);

        RoomDto roomDto = BuildersWrapper.buildRoomDto(2L, "wonderland@yahoo.com", "Wonderland", Collections.EMPTY_SET, 5, 14, UserType.ROOM);
        List<RoomDto> expectedRoomDtoList = Collections.singletonList(roomDto);

        Calendar startDate = Calendar.getInstance();
        startDate.set(2019, Calendar.JUNE, 22, 10, 0, 0);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2019, Calendar.JUNE, 22, 11, 30, 0);

        when(userRepository.viewByFields(startDate, endDate)).thenReturn(userEntityList);
        when(roomDtoMapper.mapEntityListToDtoList(userEntityList)).thenReturn(expectedRoomDtoList);

        List<RoomDto> actualRoomDtoList = sut.getRoomsByFilters(startDate, endDate, null, null);

        Assert.assertEquals(expectedRoomDtoList, actualRoomDtoList);

    }

    @Test
    public void shouldReturnRoomListWithNoReservationsInTimePeriodWhenGetRoomByFiltersIsCalledWithPresentOrFutureDates() {

        RoomEntity userEntityRoom = (RoomEntity) BuildersWrapper.buildRoomEntity(1L, "wonderland@yahoo.com", "4wonD2C%",
                null, BuildersWrapper.buildFloorEntity(1L, 5), UserType.ROOM, "Wonderland", 14);
        List<UserEntity> userEntityList = Collections.singletonList(userEntityRoom);

        RoomDto roomDto = BuildersWrapper.buildRoomDto(2L, "wonderland@yahoo.com", "Wonderland", Collections.EMPTY_SET, 5, 14, UserType.ROOM);
        List<RoomDto> expectedRoomDtoList = Collections.singletonList(roomDto);

        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MINUTE, 10);
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MINUTE, 10);

        when(userRepository.filterByFields(startDate, endDate, null, null)).thenReturn(userEntityList);
        when(roomDtoMapper.mapEntityListToDtoList(userEntityList)).thenReturn(expectedRoomDtoList);

        List<RoomDto> actualRoomDtoList = sut.getRoomsByFilters(startDate, endDate, null, null);

        Assert.assertEquals(expectedRoomDtoList, actualRoomDtoList);
    }

    @Test
    public void shouldReturnRoomListWithNoDuplicatesWhenGetRoomByFiltersIsCalledWithPresentOrFutureDatesAndRepositoryReturnsDuplicateData() {

        RoomEntity userEntityRoom = (RoomEntity) BuildersWrapper.buildRoomEntity(2L, "wonderland@yahoo.com", "wonderland",
                null, BuildersWrapper.buildFloorEntity(1L, 5), UserType.ROOM, "Wonderland", 14);
        UserEntity userEntityRoomUnique = BuildersWrapper.buildRoomEntity(3L, "westeros@yahoo.com", "westeros",
                null, BuildersWrapper.buildFloorEntity(2L, 8), UserType.ROOM, "Westeros", 20);
        List<UserEntity> userEntityList = Arrays.asList(userEntityRoom, userEntityRoom, userEntityRoom, userEntityRoomUnique);

        RoomDto roomDtoOne = BuildersWrapper.buildRoomDto(2L, "wonderland@yahoo.com", "Wonderland", Collections.EMPTY_SET, 5, 14, UserType.ROOM);
        RoomDto roomDtoTwo = BuildersWrapper.buildRoomDto(3L, "westeros@yahoo.com", "Westeros", Collections.EMPTY_SET, 8, 20, UserType.ROOM);
        List<RoomDto> expectedRoomDtoList = Arrays.asList(roomDtoOne, roomDtoTwo);

        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MINUTE, 10);
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MINUTE, 10);

        List<UserEntity> filteredUserEntityList = Arrays.asList(userEntityRoom, userEntityRoomUnique);

        when(userRepository.filterByFields(startDate, endDate, null, null)).thenReturn(userEntityList);
        when(roomDtoMapper.mapEntityListToDtoList(filteredUserEntityList)).thenReturn(expectedRoomDtoList);

        List<RoomDto> actualRoomDtoList = sut.getRoomsByFilters(startDate, endDate, null, null);

        Assert.assertEquals(expectedRoomDtoList, actualRoomDtoList);
    }

    @Test
    public void shouldReturnSameUserEntitiesWithFilteredReservations() {
        RoomEntity roomEntityOne = (RoomEntity) BuildersWrapper.buildRoomEntity(1L, "wonderland@yahoo.com", "4wonD2C%",
                new HashSet<>(), BuildersWrapper.buildFloorEntity(1L, 5), UserType.ROOM, "Wonderland", 14);
        Set<ReservationEntity> reservationEntitySet = buildReservationEntitySet(roomEntityOne);
        roomEntityOne.setReservations(reservationEntitySet);
        UserEntity roomEntityTwo = BuildersWrapper.buildRoomEntity(3L, "westeros@yahoo.com", "4westAD8%",
                reservationEntitySet, BuildersWrapper.buildFloorEntity(1L, 5), UserType.ROOM, "Westeros", 20);

        List<UserEntity> roomEntityList = Arrays.asList(roomEntityOne, roomEntityTwo);

        PersonEntity personEntity = (PersonEntity) BuildersWrapper.buildPersonEntity(2L, "sghitun@yahoo.com", "sghitun",
                new HashSet<>(), UserType.PERSON, "Ghitun", "Stefania");

        List<UserEntity> expected = buildUserEntitiesWithFilteredReservations(roomEntityList);

        when(tokenParserService.getEmailFromToken()).thenReturn("sghitun@yahoo.com");
        when(userRepository.findByEmail("sghitun@yahoo.com")).thenReturn(Optional.of(personEntity));

        List<UserEntity> actual = sut.updateUserEntitiesReservation(roomEntityList);

        Assert.assertEquals(expected, actual);
    }

    private List<UserEntity> buildUserEntitiesWithFilteredReservations(List<UserEntity> userEntities) {
        List<UserEntity> result = new ArrayList<>(userEntities);
        for (UserEntity userEntity : result) {
            Set<ReservationEntity> reservationEntities = setFilterReservationDescription(((RoomEntity) userEntity).getReservations());
            ((RoomEntity) userEntity).setReservations(reservationEntities);
        }
        return result;
    }

    private Set<ReservationEntity> setFilterReservationDescription(Set<ReservationEntity> reservationEntitySet) {
        Set<ReservationEntity> result = new HashSet<>(reservationEntitySet);
        for (ReservationEntity reservationEntity : result) {
            if (reservationEntity.getPerson().getId().equals(SECOND_PERSON_ID)) {
                reservationEntity.setDescription(null);
            }
        }
        return result;
    }

    private Set<ReservationEntity> buildReservationEntitySet(UserEntity roomEntity) {
        PersonEntity personEntity = (PersonEntity) BuildersWrapper.buildPersonEntity(2L, "sghitun@yahoo.com", "sghitun",
                new HashSet<>(), UserType.PERSON, "Ghitun", "Stefania");
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
        for (ReservationEntity reservationEntity : reservationEntities) {
            if (reservationEntity.getPerson().getId().equals(SECOND_PERSON_ID)) {
                reservationEntity.setDescription(null);
            }
            ReservationDto reservationDto = reservationDtoMapper.mapReservationEntityToDto(reservationEntity);
            reservationDtos.add(reservationDto);
        }
        return reservationDtos;
    }
}
