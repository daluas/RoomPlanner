package edu.roomplanner.service;

import edu.roomplanner.builders.UserEntityBuilder;
import edu.roomplanner.exception.InvalidReservationDtoException;
import edu.roomplanner.exception.RoomNotFoundException;
import edu.roomplanner.exception.UserAuthorityException;
import edu.roomplanner.exception.UserNotFoundException;
import edu.roomplanner.repository.UserRepository;
import edu.roomplanner.service.impl.PrevalidationServiceImpl;
import edu.roomplanner.types.UserType;
import edu.roomplanner.validation.validator.impl.StartEndDateValidator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Calendar;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class PrevalidationServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private StartEndDateValidator startEndDateValidator;

    @InjectMocks
    private PrevalidationServiceImpl sut;

    @Test(expected = InvalidReservationDtoException.class)
    public void shouldReturnInvalidNullParameters() {
        sut.prevalidate(null, null, null, null);
    }

    @Test(expected = UserNotFoundException.class)
    public void shouldReturnInvalidPersonEmailParameters() {
        String email = "invalidEmail@yahoo.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        sut.prevalidate(Calendar.getInstance(), Calendar.getInstance(), email, 1L);
    }

    @Test(expected = UserAuthorityException.class)
    public void shouldReturnInvalidPersonTypeParameters() {
        String email = "email@yahoo.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.ofNullable(UserEntityBuilder.builder().withId(1L).withType(UserType.ROOM).build()));

        sut.prevalidate(Calendar.getInstance(), Calendar.getInstance(), email, 1L);
    }

    @Test(expected = RoomNotFoundException.class)
    public void shouldReturnInvalidRoomIdParameters() {
        String email = "email@yahoo.com";
        Long roomId = 2L;

        when(userRepository.findByEmail(email)).thenReturn(Optional.ofNullable(UserEntityBuilder.builder().withId(1L).withType(UserType.PERSON).build()));
        when(userRepository.findById(roomId)).thenReturn(Optional.empty());

        sut.prevalidate(Calendar.getInstance(), Calendar.getInstance(), email, roomId);
    }

    @Test(expected = UserAuthorityException.class)
    public void shouldReturnInvalidRoomTypeParameters() {
        String email = "email@yahoo.com";
        Long roomId = 2L;

        when(userRepository.findByEmail(email)).thenReturn(Optional.ofNullable(UserEntityBuilder.builder().withId(1L).withType(UserType.PERSON).build()));
        when(userRepository.findById(roomId)).thenReturn(Optional.ofNullable(UserEntityBuilder.builder().withId(roomId).withType(UserType.PERSON).build()));

        sut.prevalidate(Calendar.getInstance(), Calendar.getInstance(), email, roomId);
    }

}