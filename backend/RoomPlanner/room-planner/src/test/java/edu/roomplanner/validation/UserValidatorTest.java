package edu.roomplanner.validation;

import edu.roomplanner.entity.RoomEntity;
import edu.roomplanner.entity.UserEntity;
import edu.roomplanner.repository.UserRepository;
import edu.roomplanner.types.UserType;
import edu.roomplanner.util.BuildersWrapper;
import edu.roomplanner.validation.validator.impl.UserValidatorImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;


@RunWith(SpringRunner.class)
public class UserValidatorTest {


    @Mock
    private UserRepository userRepository;


    @InjectMocks
    private UserValidatorImpl sut;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldReturnTrueWhenCheckValidRoomIdIsCalledWithInjectedRoomId() {

        UserEntity userEntity = BuildersWrapper.buildRoomEntity(1L, "wonderland@yahoo.com", "wonderland",
                null, null, UserType.ROOM, "Wonderland", 14);

        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(userEntity));

        boolean response = sut.checkValidRoomId(1L);

        assertTrue(response);
    }

    @Test
    public void shouldReturnFalseWhenCheckValidRoomIdIsCalledWithInjectedPersonId() {
        UserEntity userEntity = BuildersWrapper.buildPersonEntity(3L, "sghitun@yahoo.com",
                "sghitun", null, UserType.PERSON, "Git", "Mast");

        when(userRepository.findById(3L)).thenReturn(Optional.ofNullable(userEntity));
        boolean response = sut.checkValidRoomId(3L);

        assertFalse(response);
    }

    @Test
    public void shouldReturnFalseWhenCheckValidRoomIdIsCalledWithNonExistentId() {

        UserEntity userEntity = new RoomEntity();
        when(userRepository.findById(3L)).thenReturn(Optional.ofNullable(userEntity));
        boolean response = sut.checkValidRoomId(7L);

        assertFalse(response);
    }

}
