package edu.roomplanner.service;

import edu.roomplanner.dto.ReservationDto;
import edu.roomplanner.entity.ReservationEntity;
import edu.roomplanner.entity.UserEntity;
import edu.roomplanner.mappers.ReservationDtoMapper;
import edu.roomplanner.repository.ReservationRepository;
import edu.roomplanner.service.impl.ReservationServiceImpl;
import edu.roomplanner.types.UserType;
import edu.roomplanner.util.BuildersWrapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Calendar;
import java.util.Optional;

import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class ReservationServiceTest {
    @Mock
    private ReservationDtoMapper mapperService;
    @Mock
    private ReservationRepository reservationRepository;
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

        Optional<ReservationDto> expectedReservationDto = Optional.of(BuildersWrapper.buildReservationDto(1L, 2L, "sghitun@yahoo.com", startDate, endDate, "description"));

        ReservationDto reservationDto = BuildersWrapper.buildReservationDto(1L, null, "sghitun@yahoo.com", startDate, endDate, "description");
        UserEntity roomEntity = BuildersWrapper.buildRoomEntity(2L, "wonderland@yahoo.com", "4wonD2C%",
                UserType.ROOM, "Wonderland", 5, 14);
        UserEntity personEntity = BuildersWrapper.buildPersonEntiy(2L, "sghitun@yahoo.com", "password",
                UserType.PERSON, "Popescu", "Ana");
        ReservationEntity reservationEntity = BuildersWrapper.buildReservationEntity(1L, startDate, endDate, personEntity, roomEntity, "description");

        when(mapperService.mapReservationDtoToEntity(reservationDto)).thenReturn(reservationEntity);
        when(reservationRepository.save(reservationEntity)).thenReturn(reservationEntity);
        when(mapperService.mapReservationEntityToDto(reservationEntity)).thenReturn(reservationDto);

        Optional<ReservationDto> actualReservationDto = sut.createReservation(2L, reservationDto);

        Assert.assertEquals(expectedReservationDto, actualReservationDto);

    }

    @Test
    public void shouldReturnEmptyReservationDtoWhenCreateReservationIsCalledWithEmptyReservationDto() {
        //Optional<ReservationDto>  expectedReservationDto = Optional.of(new ReservationDto());
        Optional<ReservationDto> expectedReservationDto = Optional.empty();
        ReservationDto reservationDto = new ReservationDto();
        Optional<ReservationDto> actualReservationDto = sut.createReservation(2L, reservationDto);

        Assert.assertEquals(expectedReservationDto, actualReservationDto);
    }


}
