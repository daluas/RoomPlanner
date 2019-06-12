package edu.roomplanner.service;

import edu.roomplanner.RoomPlannerApplication;
import edu.roomplanner.dto.RoomDto;
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

import java.util.Arrays;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = RoomPlannerApplication.class
)
@TestPropertySource(locations = "classpath:application-test.properties")
public class UserServiceTests {

    @Autowired
    private UserService userService;

    @Autowired
    private Flyway flyway;

    @Before
    public void init() {
        flyway.clean();
        flyway.migrate();
    }

    @Autowired
    private UserRepository userRepository;

    @Test
    public void ShouldReturnRoomDtoListWhenGetAllRoomsIsCalled() {

        RoomDto testRoomDto1 = BuilderClass.buildRoomDto(1L, "Wonderland", 5, 14);
        RoomDto testRoomDto2 = BuilderClass.buildRoomDto(2L, "Westeros", 8, 21);
        List<RoomDto> expectedRoomDtoList = Arrays.asList(testRoomDto1, testRoomDto2);

        UserEntity testUserEntity1 = BuilderClass.buildRoomEntity(1L,"wonderland@yahoo.com","4wonD2C%",UserType.ROOM,"Wonderland",5,14);
        UserEntity testUserEntity2 = BuilderClass.buildRoomEntity(2L,"westeros@yahoo.com","4westAD8%",UserType.ROOM,"Westeros",8,21);

        userRepository.save(testUserEntity1);
        userRepository.save(testUserEntity2);

        List<RoomDto> actualRoomDtoList = userService.getAllRooms();

        Assert.assertEquals("GetAllRooms service method failed to return a valid list.", expectedRoomDtoList, actualRoomDtoList);
    }

    @Test
    public void ShouldReturnExpectedRoomDtoWhenGetRoomByIdIsCalled() {

        UserEntity testUserEntity1 = BuilderClass.buildRoomEntity(1L,"wonderland@yahoo.com","4wonD2C%",UserType.ROOM,"Wonderland",5,14);
        userRepository.save(testUserEntity1);

        RoomDto expectedRoomDto = BuilderClass.buildRoomDto(1L, "Wonderland", 5, 14);
        RoomDto actualRoomDto = userService.getRoomById(1L);

        Assert.assertEquals("GetRoomById service method method failed to return a valid object.", expectedRoomDto, actualRoomDto);
    }
}
