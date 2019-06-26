package edu.roomplanner.validation;

import edu.roomplanner.entity.UserEntity;
import edu.roomplanner.repository.UserRepository;
import edu.roomplanner.service.TokenParserService;
import edu.roomplanner.types.UserType;
import edu.roomplanner.util.BuildersWrapper;
import edu.roomplanner.validation.validator.impl.UserRightsValidatorImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc(secure = false)
public class UserRightsValidatorTest {

    @Mock
    private TokenParserService tokenParserService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserRightsValidatorImpl sut;

    @Test
    public void shouldReturnTrueIfCurrentUserIsRoom() {

        UserEntity roomEntity = BuildersWrapper.buildRoomEntity(1L, "wonderland@yahoo.com", "wonderland",
                UserType.ROOM, "Wonderland", 5, 14);

        roomEntity.setRoles(null);
        roomEntity.setReservations(null);
        String email = "wonderland@yahoo.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(roomEntity));
        when(tokenParserService.getEmailFromToken()).thenReturn(email);

        boolean response = sut.checkIfUserIsRoom();

        assertTrue(response);
    }

    @Test
    public void shouldReturnFalseIfCurrentUserIsNotRoom(){

        UserEntity userEntity = BuildersWrapper.buildPersonEntiy(3L, "sghitun@yahoo.com",
                "sghitun", UserType.PERSON, "Git", "Mast");

        userEntity.setRoles(null);
        userEntity.setReservations(null);
        String email = "wonderland@yahoo.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(userEntity));
        when(tokenParserService.getEmailFromToken()).thenReturn(email);

        boolean response = sut.checkIfUserIsRoom();

        assertFalse(response);
    }

    @Test
    public void shouldReturnTrueIfCurrentRoomIdEqualsRequestedRoomId(){

        UserEntity userEntity = BuildersWrapper.buildPersonEntiy(3L, "sghitun@yahoo.com",
                "sghitun", UserType.PERSON, "Git", "Mast");

        userEntity.setRoles(null);
        userEntity.setReservations(null);
        String email = "wonderland@yahoo.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(userEntity));
        when(tokenParserService.getEmailFromToken()).thenReturn(email);

        boolean response = sut.checkIfLoggedRoomIsRequestedRoom(3L);

        assertTrue(response);
    }

    @Test
    public void shouldReturnFalseIfCurrentRoomIdEqualsRequestedRoomId(){

        UserEntity userEntity = BuildersWrapper.buildPersonEntiy(3L, "sghitun@yahoo.com",
                "sghitun", UserType.PERSON, "Git", "Mast");

        userEntity.setRoles(null);
        userEntity.setReservations(null);
        String email = "wonderland@yahoo.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(userEntity));
        when(tokenParserService.getEmailFromToken()).thenReturn(email);

        boolean response = sut.checkIfLoggedRoomIsRequestedRoom(2L);

        assertFalse(response);
    }
}
