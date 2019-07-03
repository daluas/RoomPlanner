package edu.roomplanner.service;

import edu.roomplanner.builders.ReservationDtoBuilder;
import edu.roomplanner.builders.ReservationEntityBuilder;
import edu.roomplanner.builders.UserEntityBuilder;
import edu.roomplanner.builders.ReservationEntityBuilder;
import edu.roomplanner.builders.UserEntityBuilder;
import edu.roomplanner.dto.ReservationDto;
import edu.roomplanner.entity.FloorEntity;
import edu.roomplanner.entity.ReservationEntity;
import edu.roomplanner.entity.UserEntity;
import edu.roomplanner.exception.InvalidReservationDtoException;
import edu.roomplanner.exception.ReservationNotFoundException;
import edu.roomplanner.exception.ReservationNotFoundException;
import edu.roomplanner.exception.UnauthorizedReservationException;
import edu.roomplanner.mappers.ReservationDtoMapper;
import edu.roomplanner.repository.ReservationRepository;
import edu.roomplanner.repository.UserRepository;
import edu.roomplanner.service.impl.ReservationServiceImpl;
import edu.roomplanner.types.UserType;
import edu.roomplanner.util.BuildersWrapper;
import edu.roomplanner.validation.BookingChain;
import edu.roomplanner.validation.ValidationResult;
import edu.roomplanner.validation.validator.impl.StartEndDateValidator;
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
public class ReservationServiceTest {
    @Mock
    private ReservationDtoMapper mapperService;
    @Mock
    private ReservationRepository reservationRepository;
    @Mock
    private BookingChain bookingChain;
    @Mock
    private TokenParserService tokenParserService;
    @Mock
    private StartEndDateValidator startEndDateValidator;
    @Mock
    private UserRepository userRepository;
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
    public void shouldReturnReservationDtoWhenUpdateReservationIsCalledWithValidId() {

        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        Calendar newStartDate = Calendar.getInstance();
        Calendar newEndDate = Calendar.getInstance();
        startDate.set(2022, Calendar.JANUARY, 6, 10, 10, 0);
        endDate.set(2022, Calendar.JANUARY, 6, 10, 45, 0);
        newStartDate.set(2025, Calendar.JANUARY, 6, 10, 10, 0);
        newEndDate.set(2025, Calendar.JANUARY, 6, 10, 45, 0);

        ReservationDto expectedReservationDto = BuildersWrapper.buildReservationDto(1L, 2L, "sghitun@yahoo.com", newStartDate, newEndDate, "reservation updated");
        ReservationDto updatedReservationDto = BuildersWrapper.buildReservationDto(1L, 2L, "sghitun@yahoo.com", newStartDate, newEndDate, "reservation updated");
        ReservationDto reservationDto = BuildersWrapper.buildReservationDto(null, null, null, newStartDate, newEndDate, "reservation updated");

        UserEntity roomEntity = BuildersWrapper.buildRoomEntity(2L, "wonderland@yahoo.com", "4wonD2C%",
                new HashSet<>(), new FloorEntity(), UserType.ROOM, "Wonderland", 14);
        UserEntity personEntity = BuildersWrapper.buildPersonEntity(1L, "sghitun@yahoo.com", "password",
                new HashSet<>(), UserType.PERSON, "Popescu", "Ana");
        Optional<ReservationEntity> reservationEntity = Optional.ofNullable(BuildersWrapper.buildReservationEntity(1L, startDate, endDate, personEntity, roomEntity, "description"));
        ReservationEntity newReservationEntity = BuildersWrapper.buildReservationEntity(1L, newStartDate, newEndDate, personEntity, roomEntity, "reservation updated");

        ReservationDto copyDto = ReservationDtoBuilder.builder().withEndDate(Calendar.getInstance()).withStartDate(Calendar.getInstance()).build();
        copyDto.getStartDate().setTime((Date) reservationDto.getStartDate().getTime().clone());
        copyDto.getEndDate().setTime((Date) reservationDto.getEndDate().getTime().clone());

        copyDto.getStartDate().setTime(conversionToGmt(reservationDto.getStartDate().getTime()));
        copyDto.getEndDate().setTime(conversionToGmt(reservationDto.getEndDate().getTime()));


        when(tokenParserService.getEmailFromToken()).thenReturn("sghitun@yahoo.com");
        when(startEndDateValidator.validate(getReservation(copyDto))).thenReturn(new ValidationResult());
        when(reservationRepository.findById(1L)).thenReturn(reservationEntity);
        when(reservationRepository.save(reservationEntity.get())).thenReturn(newReservationEntity);
        when(userRepository.findByEmail("sghitun@yahoo.com")).thenReturn(Optional.of(UserEntityBuilder.builder().withId(1L).withType(UserType.PERSON).build()));
        when(mapperService.mapReservationEntityToDto(newReservationEntity)).thenReturn(updatedReservationDto);

        ReservationDto actualReservationDto = sut.updateReservation(1L, reservationDto);

        Assert.assertEquals(expectedReservationDto, actualReservationDto);

    }

    @Test(expected = ReservationNotFoundException.class)
    public void shouldReturnEmptyReservationDtoWhenUpdateReservationIsCalledWithInvalidId() {
        ReservationDto reservationDto = new ReservationDto();
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        startDate.set(2025, Calendar.JANUARY, 6, 10, 10, 0);
        endDate.set(2025, Calendar.JANUARY, 6, 10, 45, 0);
        reservationDto.setStartDate(startDate);
        reservationDto.setEndDate(endDate);
        reservationDto.setDescription("description");
        sut.updateReservation(255L, reservationDto);

    }

    private ReservationEntity getReservation(ReservationDto reservationDto) {
        Calendar startDate = reservationDto.getStartDate();
        Calendar endDate = reservationDto.getEndDate();
        return ReservationEntityBuilder.builder()
                .withStartDate(startDate)
                .withEndDate(endDate)
                .build();
    }

    private Date conversionToGmt(Date date) {
        TimeZone tz = TimeZone.getDefault();
        Date ret = new Date(date.getTime() - tz.getRawOffset());
        if (tz.inDaylightTime(ret)) {
            Date dstDate = new Date(ret.getTime() - tz.getDSTSavings());
            if (tz.inDaylightTime(dstDate)) {
                ret = dstDate;
            }
        }
        return ret;
    }
    @Test(expected = ReservationNotFoundException.class)
    public void shouldNotFindValidReservationId() {
        Long roomId = 4L;

        when(reservationRepository.findById(roomId)).thenReturn(Optional.empty());
        sut.deleteReservation(roomId);
    }

    @Test(expected = UnauthorizedReservationException.class)
    public void shouldFindThatAPersonWantsToDeleteOtherUserReservation() {
        Long roomId = 4L;
        String email = "sghitun@yahoo.com";

        when(tokenParserService.getEmailFromToken()).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(getPerson(6L)));
        when(reservationRepository.findById(roomId)).thenReturn(Optional.of(getReservation(5L)));
        sut.deleteReservation(roomId);

    }

    @Test
    public void shouldDeleteReservation() {
        Long roomId = 4L;
        String email = "sghitun@yahoo.com";

        when(tokenParserService.getEmailFromToken()).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(getPerson(6L)));
        when(reservationRepository.findById(roomId)).thenReturn(Optional.of(getReservation(6L)));
        sut.deleteReservation(roomId);

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

