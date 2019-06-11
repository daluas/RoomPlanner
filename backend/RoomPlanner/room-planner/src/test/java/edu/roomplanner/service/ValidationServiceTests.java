package edu.roomplanner.service;

import edu.roomplanner.RoomPlannerApplication;
import org.junit.Assert;
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
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:scripts/init_h2db.sql")
@TestPropertySource(locations = "classpath:application-test.properties")
public class ValidationServiceTests {

    @Autowired
    private ValidationService validationService;

    @Test
    public void Should_Return_True_When_CheckValidRoomId_Is_Called_With_Injected_Room_Id() {

        boolean response = validationService.checkValidRoomId(2L);

        Assert.assertTrue("CheckValidRoomId failed to check valid for Room Id.", response);
    }

    @Test
    public void Should_Return_False_When_CheckValidRoomId_Is_Called_With_Injected_Person_Id() {

        boolean response = validationService.checkValidRoomId(1L);

        Assert.assertFalse("CheckValidRoomId failed to check invalid for Person Id.", response);
    }

    @Test
    public void Should_Return_False_When_CheckValidRoomId_Is_Called_With_NonExistent_Id() {

        boolean response = validationService.checkValidRoomId(7L);

        Assert.assertFalse("CheckValidRoomId failed to check invalid for NonExistent Id.", response);
    }

    @Test
    public void Should_Return_True_When_CheckExistingRoomId_Is_Called_With_Valid_Id() {

        boolean response = validationService.checkExistingRoomId(2L);

        Assert.assertTrue("CheckExistingRoomId failed to check valid for Room Id.", response);
    }

    @Test
    public void Should_Return_False_When_CheckExistingRoomId_Is_Called_With_NonExisting_Id() {

        boolean response = validationService.checkExistingRoomId(7L);

        Assert.assertFalse("CheckExistingRoomId failed to check invalid for NonExistent Id.", response);
    }
}
