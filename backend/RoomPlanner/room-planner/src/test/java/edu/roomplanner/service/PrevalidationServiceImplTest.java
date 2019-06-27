package edu.roomplanner.service;

import edu.roomplanner.builders.UserEntityBuilder;
import edu.roomplanner.repository.UserRepository;
import edu.roomplanner.service.impl.PrevalidationServiceImpl;
import edu.roomplanner.types.UserType;
import edu.roomplanner.validation.validator.impl.StartEndDateValidator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Calendar;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class PrevalidationServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private StartEndDateValidator startEndDateValidator;

    @InjectMocks
    private PrevalidationServiceImpl sut;

    @Test
    public void shouldReturnInvalidNullParameters() {
        Integer expected = 400;
        Integer actual = sut.prevalidate(null, null, null, null);

        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnInvalidPersonEmailParameters() {
        Integer expected = 400;
        String email = "invalidEmail@yahoo.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        Integer actual = sut.prevalidate(Calendar.getInstance(), Calendar.getInstance(), email, 1L);

        assertEquals(expected, actual);

    }

    @Test
    public void shouldReturnInvalidPersonTypeParameters() {
        Integer expected = 400;
        String email = "email@yahoo.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.ofNullable(UserEntityBuilder.builder().withId(1L).withType(UserType.ROOM).build()));

        Integer actual = sut.prevalidate(Calendar.getInstance(), Calendar.getInstance(), email, 1L);

        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnInvalidRoomIdParameters() {
        Integer expected = 400;
        String email = "email@yahoo.com";
        Long roomId = 2L;

        when(userRepository.findByEmail(email)).thenReturn(Optional.ofNullable(UserEntityBuilder.builder().withId(1L).withType(UserType.PERSON).build()));
        when(userRepository.findById(roomId)).thenReturn(Optional.empty());

        Integer actual = sut.prevalidate(Calendar.getInstance(), Calendar.getInstance(), email, roomId);

        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnInvalidRoomTypeParameters() {
        Integer expected = 400;
        String email = "email@yahoo.com";
        Long roomId = 2L;

        when(userRepository.findByEmail(email)).thenReturn(Optional.ofNullable(UserEntityBuilder.builder().withId(1L).withType(UserType.PERSON).build()));
        when(userRepository.findById(roomId)).thenReturn(Optional.ofNullable(UserEntityBuilder.builder().withId(roomId).withType(UserType.PERSON).build()));

        Integer actual = sut.prevalidate(Calendar.getInstance(), Calendar.getInstance(), email, roomId);

        assertEquals(expected, actual);
    }

}