package edu.roomplanner.service;

import edu.roomplanner.RoomPlannerApplication;
import edu.roomplanner.dto.RoomDto;
import edu.roomplanner.entity.UserEntity;
import edu.roomplanner.repository.UserRepository;
import edu.roomplanner.types.UserType;
import edu.roomplanner.builders.RoomDtoBuilder;
import edu.roomplanner.builders.RoomEntityBuilder;
import org.flywaydb.core.Flyway;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
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
    public void shouldReturnRoomDtoListWhenGetAllRoomsIsCalled() {

        RoomDto roomDtoOne = new RoomDtoBuilder()
                .withId(1L)
                .withName("Wonderland")
                .withFloor(5)
                .withMaxPersons(14)
                .build();
        RoomDto roomDtoTwo = new RoomDtoBuilder()
                .withId(2L)
                .withName("Westeros")
                .withFloor(8)
                .withMaxPersons(21)
                .build();

        List<RoomDto> expectedRoomDtoList = Arrays.asList(roomDtoOne, roomDtoTwo);

        UserEntity userEntityOne = new RoomEntityBuilder()
                .withId(1L)
                .withEmail("wonderland@yahoo.com")
                .withPassword("4wonD2C%")
                .withType(UserType.ROOM)
                .withName("Wonderland")
                .withFloor(5)
                .withMaxPersons(14)
                .build();

        UserEntity userEntityTwo = new RoomEntityBuilder()
                .withId(2L)
                .withEmail("westeros@yahoo.com")
                .withPassword("4westAD8%")
                .withType(UserType.ROOM)
                .withName("Westeros")
                .withFloor(8)
                .withMaxPersons(21)
                .build();

        userRepository.save(userEntityOne);
        userRepository.save(userEntityTwo);

        List<RoomDto> actualRoomDtoList = userService.getAllRooms();

        Assert.assertEquals("GetAllRooms service method failed to return a valid list.", expectedRoomDtoList, actualRoomDtoList);
    }

    @Test
    public void shouldReturnExpectedRoomDtoWhenGetRoomByIdIsCalled() {

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

        RoomDto expectedRoomDto = new RoomDtoBuilder()
                .withId(1L)
                .withName("Wonderland")
                .withFloor(5)
                .withMaxPersons(14)
                .build();
        RoomDto actualRoomDto = userService.getRoomById(1L);

        Assert.assertEquals("GetRoomById service method method failed to return a valid object.", expectedRoomDto, actualRoomDto);
    }
}
