package edu.roomplanner.service;

import edu.roomplanner.RoomPlannerApplication;
import edu.roomplanner.entity.UserEntity;
import edu.roomplanner.repository.UserRepository;
import edu.roomplanner.types.UserType;
import edu.roomplanner.builders.PersonEntityBuilder;
import edu.roomplanner.builders.RoomEntityBuilder;
import edu.roomplanner.validation.UserValidation;
import org.flywaydb.core.Flyway;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = RoomPlannerApplication.class
)
@TestPropertySource(locations = "classpath:application-test.properties")
public class UserValidationTests {

    @Autowired
    private UserValidation userValidation;

    @Autowired
    private Flyway flyway;

    @Autowired
    private UserRepository userRepository;

    @Before
    public void init() {
        flyway.clean();
        flyway.migrate();
    }

    @Test
    public void shouldReturnTrueWhenCheckValidRoomIdIsCalledWithInjectedRoomId() {

        UserEntity userEntity = new RoomEntityBuilder()
                .withId(1L)
                .withEmail("wonderland@yahoo.com")
                .withPassword("4wonD2C%")
                .withType(UserType.ROOM)
                .withName("Wonderland")
                .withFloor(5)
                .withMaxPersons(14)
                .build();
        userRepository.save(userEntity);
        boolean response = userValidation.checkValidRoomId(1L);

        Assert.assertTrue("CheckValidRoomId failed to check valid for Room Id.", response);
    }

    @Test
    public void shouldReturnFalseWhenCheckValidRoomIdIsCalledWithInjectedPersonId() {

        UserEntity userEntity = new PersonEntityBuilder()
                .withId(3L)
                .withEmail("sgitMast@yahoo.com")
                .withPassword("sajhsar2A%")
                .withType(UserType.PERSON)
                .withFirstName("Git")
                .withLastName("Mast")
                .build();
        userRepository.save(userEntity);
        boolean response = userValidation.checkValidRoomId(3L);

        Assert.assertFalse("CheckValidRoomId failed to check invalid for Person Id.", response);
    }

    @Test
    public void shouldReturnFalseWhenCheckValidRoomIdIsCalledWithNonExistentId() {

        boolean response = userValidation.checkValidRoomId(7L);

        Assert.assertFalse("CheckValidRoomId failed to check invalid for NonExistent Id.", response);
    }

    @Test
    public void shouldReturnTrueWhenCheckExistingRoomIdIsCalledWithValidId() {

        UserEntity userEntity = new RoomEntityBuilder()
                .withId(1L)
                .withEmail("wonderland@yahoo.com")
                .withPassword("4wonD2C%")
                .withType(UserType.ROOM)
                .withName("Wonderland")
                .withFloor(5)
                .withMaxPersons(14)
                .build();
        userRepository.save(userEntity);
        boolean response = userValidation.checkExistingRoomId(1L);

        Assert.assertTrue("CheckExistingRoomId failed to check valid for Room Id.", response);
    }

    @Test
    public void shouldReturnFalseWhenCheckExistingRoomIdIsCalledWithNonExistingId() {

        boolean response = userValidation.checkExistingRoomId(7L);

        Assert.assertFalse("CheckExistingRoomId failed to check invalid for NonExistent Id.", response);
    }
}
