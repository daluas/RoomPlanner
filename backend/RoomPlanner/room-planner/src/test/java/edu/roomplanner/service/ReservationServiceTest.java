package edu.roomplanner.service;

import edu.roomplanner.builders.ReservationEntityBuilder;
import edu.roomplanner.builders.UserEntityBuilder;
import edu.roomplanner.dto.ReservationDto;
import edu.roomplanner.entity.FloorEntity;
import edu.roomplanner.entity.ReservationEntity;
import edu.roomplanner.entity.UserEntity;
import edu.roomplanner.exception.InvalidReservationDtoException;
import edu.roomplanner.mappers.ReservationDtoMapper;
import edu.roomplanner.repository.ReservationRepository;
import edu.roomplanner.repository.UserRepository;
import edu.roomplanner.service.impl.ReservationServiceImpl;
import edu.roomplanner.types.UserType;
import edu.roomplanner.util.BuildersWrapper;
import edu.roomplanner.validation.BookingChain;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Optional;

import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class ReservationServiceTest {

    @Mock
    private TokenParserService tokenParserService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ReservationDtoMapper mapperService;
    @Mock
    private ReservationRepository reservationRepository;
    @Mock
    private BookingChain bookingChain;
    @InjectMocks
    private ReservationServiceImpl sut;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldReturnReservationDtoWhenCreateReservationIsCalled() {

        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        startDate.set(2019, Calendar.JANUARY, 6, 10, 10, 0);
        endDate.set(2019, Calendar.JANUARY, 6, 10, 45, 0);
        ReservationDto expectedReservationDto = BuildersWrapper.buildReservationDto(1L, 2L, "sghitun@yahoo.com", startDate, endDate, "description");
        ReservationDto reservationDto = BuildersWrapper.buildReservationDto(1L, null, "sghitun@yahoo.com", startDate, endDate, "description");
        UserEntity roomEntity = BuildersWrapper.buildRoomEntity(2L, "wonderland@yahoo.com", "4wonD2C%",
                new HashSet<>(), new FloorEntity(), UserType.ROOM, "Wonderland", 14);
        UserEntity personEntity = BuildersWrapper.buildPersonEntity(2L, "sghitun@yahoo.com", "password",
                new HashSet<>(), UserType.PERSON, "Popescu", "Ana");
        ReservationEntity reservationEntity = BuildersWrapper.buildReservationEntity(1L, startDate, endDate, personEntity, roomEntity, "description");
        when(mapperService.mapReservationDtoToEntity(reservationDto)).thenReturn(reservationEntity);
        when(reservationRepository.save(reservationEntity)).thenReturn(reservationEntity);
        when(mapperService.mapReservationEntityToDto(reservationEntity)).thenReturn(reservationDto);
        ReservationDto actualReservationDto = sut.createReservation(2L, reservationDto);

        Assert.assertEquals(expectedReservationDto, actualReservationDto);

    }

    @Test(expected = InvalidReservationDtoException.class)
    public void shouldReturnEmptyReservationDtoWhenCreateReservationIsCalledWithEmptyReservationDto() throws Exception {
        ReservationDto reservationDto = new ReservationDto();
        sut.createReservation(2L, reservationDto);
    }

    @Test
    public void shouldNotFindValidReservationId() {
        HttpStatus expected = HttpStatus.NOT_FOUND;
        Long roomId = 4L;

        when(reservationRepository.findById(roomId)).thenReturn(Optional.empty());
        HttpStatus actual = sut.deleteReservation(roomId);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void shouldFindThatAPersonWantsToDeleteOtherUserReservation() {
        HttpStatus expected = HttpStatus.NOT_FOUND;
        Long roomId = 4L;
        String email = "sghitun@yahoo.com";

        when(tokenParserService.getEmailFromToken()).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(getPerson(6L)));
        when(reservationRepository.findById(roomId)).thenReturn(Optional.of(getReservation(5L)));
        HttpStatus actual = sut.deleteReservation(roomId);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void shouldDeleteReservation() {
        HttpStatus expected = HttpStatus.OK;
        Long roomId = 4L;
        String email = "sghitun@yahoo.com";

        when(tokenParserService.getEmailFromToken()).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(getPerson(6L)));
        when(reservationRepository.findById(roomId)).thenReturn(Optional.of(getReservation(6L)));
        HttpStatus actual = sut.deleteReservation(roomId);

        Assert.assertEquals(expected, actual);
    }


    private UserEntity getPerson(Long personId) {
        return UserEntityBuilder.builder()
                .withType(UserType.PERSON)
                .withId(personId)
                .build();
    }

    private ReservationEntity getReservation(Long personId) {
        return ReservationEntityBuilder.builder()
                .withId(4L)
                .withPerson(getPerson(personId))
                .build();
    }
}

