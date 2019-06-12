package edu.roomplanner.service;

import edu.roomplanner.RoomPlannerApplication;
import edu.roomplanner.dto.RoomDto;
import edu.roomplanner.utils.BuilderClass;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = RoomPlannerApplication.class
)
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:scripts/inith2db.sql")
@TestPropertySource(locations = "classpath:application-test.properties")
public class UserServiceTests {

    @Autowired
    private UserService userService;

    @Test
    public void ShouldReturnRoomDtoListWhenGetAllRoomsIsCalled() {

        RoomDto testRoomDto1 = BuilderClass.buildRoomDto(2L, "Wonderland", 5, 14);
        RoomDto testRoomDto2 = BuilderClass.buildRoomDto(3L, "Westeros", 8, 21);

        List<RoomDto> actualRoomDtoList = userService.getAllRooms();
        List<RoomDto> expectedRoomDtoList = Arrays.asList(testRoomDto1, testRoomDto2);

        Assert.assertEquals("GetAllRooms service method failed to return a valid list.", expectedRoomDtoList, actualRoomDtoList);
    }

    @Test
    public void ShouldReturnExpectedRoomDtoWhenGetRoomByIdIsCalled() {

        RoomDto expectedRoomDto = BuilderClass.buildRoomDto(2L, "Wonderland", 5, 14);
        RoomDto actualRoomDto = userService.getRoomById(2L);

        Assert.assertEquals("GetRoomById service method method failed to return a valid object.", expectedRoomDto, actualRoomDto);
    }
}
