package edu.roomplanner.service;

import edu.roomplanner.RoomPlannerApplication;
import edu.roomplanner.entity.UserEntity;
import edu.roomplanner.repository.UserRepository;
import edu.roomplanner.types.UserType;
import edu.roomplanner.utils.BuilderClass;
import org.flywaydb.core.Flyway;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = RoomPlannerApplication.class
)
@TestPropertySource(locations = "classpath:application-test.properties")
public class ValidationServiceTests {

    @Autowired
    private ValidationService validationService;

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
    public void ShouldReturnTrueWhenCheckValidRoomIdIsCalledWithInjectedRoomId() {

        UserEntity testUserEntity1 = BuilderClass.buildRoomEntity(1L,"wonderland@yahoo.com","4wonD2C%",UserType.ROOM,"Wonderland",5,14);
        userRepository.save(testUserEntity1);
        boolean response = validationService.checkValidRoomId(1L);

        Assert.assertTrue("CheckValidRoomId failed to check valid for Room Id.", response);
    }

    @Test
    public void ShouldReturnFalseWhenCheckValidRoomIdIsCalledWithInjectedPersonId() {

        UserEntity testUserEntity1 = BuilderClass.buildPersonEntity(3L,"sgitMast@yahoo.com","sajhsar2A%",UserType.PERSON,"Git", "Mast");
        userRepository.save(testUserEntity1);
        boolean response = validationService.checkValidRoomId(3L);

        Assert.assertFalse("CheckValidRoomId failed to check invalid for Person Id.", response);
    }

    @Test
    public void ShouldReturnFalseWhenCheckValidRoomIdIsCalledWithNonExistentId() {

        boolean response = validationService.checkValidRoomId(7L);

        Assert.assertFalse("CheckValidRoomId failed to check invalid for NonExistent Id.", response);
    }

    @Test
    public void ShouldReturnTrueWhenCheckExistingRoomIdIsCalledWithValidId() {

        UserEntity testUserEntity1 = BuilderClass.buildRoomEntity(1L,"wonderland@yahoo.com","4wonD2C%",UserType.ROOM,"Wonderland",5,14);
        userRepository.save(testUserEntity1);
        boolean response = validationService.checkExistingRoomId(1L);

        Assert.assertTrue("CheckExistingRoomId failed to check valid for Room Id.", response);
    }

    @Test
    public void ShouldReturnFalseWhenCheckExistingRoomIdIsCalledWithNonExistingId() {

        boolean response = validationService.checkExistingRoomId(7L);

        Assert.assertFalse("CheckExistingRoomId failed to check invalid for NonExistent Id.", response);
    }
}
