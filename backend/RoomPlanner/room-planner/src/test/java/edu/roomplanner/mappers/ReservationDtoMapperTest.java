package edu.roomplanner.mappers;

import edu.roomplanner.dto.ReservationDto;
import edu.roomplanner.entity.FloorEntity;
import edu.roomplanner.entity.ReservationEntity;
import edu.roomplanner.entity.UserEntity;
import edu.roomplanner.mappers.impl.ReservationDtoMapperImpl;
import edu.roomplanner.repository.UserRepository;
import edu.roomplanner.types.UserType;
import edu.roomplanner.util.BuildersWrapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Calendar;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class ReservationDtoMapperTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ReservationDtoMapperImpl sut;

    @Test
    public void shouldReturnReservationDtoWhenMapReservationEntityToDtoIsCalledWithValidReservationEntity() {

        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        startDate.set(2019, Calendar.JANUARY, 6, 10, 10, 0);
        endDate.set(2019, Calendar.JANUARY, 6, 10, 45, 0);

        UserEntity roomEntity = BuildersWrapper.buildRoomEntity(1L, "wonderland@yahoo.com", "4wonD2C%",
                new HashSet<>(), new FloorEntity(), UserType.ROOM, "Wonderland", 14);
        UserEntity personEntity = BuildersWrapper.buildPersonEntity(1L, "sghitun@yahoo.com", "password",
                new HashSet<>(), UserType.PERSON, "Popescu", "Ana");

        ReservationEntity reservationEntity = BuildersWrapper.buildReservationEntity(1L, startDate, endDate, personEntity, roomEntity, "description");
        ReservationDto expectedReservationDto = BuildersWrapper.buildReservationDto(1L, 1L, "sghitun@yahoo.com", startDate, endDate, "description");
        ReservationDto actualReservationDto = sut.mapReservationEntityToDto(reservationEntity);

        assertEquals(expectedReservationDto, actualReservationDto);

    }

    @Test
    public void shouldReturnReservationEntityWhenMapReservationDtoToEntityIsCalledWithValidReservationDto() {

        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        startDate.set(2019, Calendar.JANUARY, 6, 10, 10, 0);
        endDate.set(2019, Calendar.JANUARY, 6, 10, 45, 0);

        UserEntity roomEntity = BuildersWrapper.buildRoomEntity(1L, "wonderland@yahoo.com", "4wonD2C%",
                new HashSet<>(), new FloorEntity(), UserType.ROOM, "Wonderland", 14);
        UserEntity personEntity = BuildersWrapper.buildPersonEntity(2L, "sghitun@yahoo.com", "password",
                new HashSet<>(), UserType.PERSON, "Popescu", "Ana");

        ReservationDto reservationDto = BuildersWrapper.buildReservationDto(1L, 1L, "sghitun@yahoo.com", startDate, endDate, "description");
        ReservationEntity expectedReservationEntity = BuildersWrapper.buildReservationEntity(1L, startDate, endDate, personEntity, roomEntity, "description");

        when(userRepository.findByEmail("sghitun@yahoo.com")).thenReturn(java.util.Optional.of(personEntity));
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(roomEntity));

        ReservationEntity actualReservationEntity = sut.mapReservationDtoToEntity(reservationDto);

        assertEquals(expectedReservationEntity, actualReservationEntity);

    }

}
