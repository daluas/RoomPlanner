package edu.roomplanner.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.roomplanner.RoomPlannerApplication;
import edu.roomplanner.dto.FloorDto;
import edu.roomplanner.dto.RoomDto;
import edu.roomplanner.dto.UserDto;
import edu.roomplanner.entity.FloorEntity;
import edu.roomplanner.entity.RoomEntity;
import edu.roomplanner.entity.UserEntity;
import edu.roomplanner.repository.FloorRepository;
import edu.roomplanner.repository.UserRepository;
import edu.roomplanner.types.UserType;
import edu.roomplanner.util.BuildersWrapper;
import edu.roomplanner.util.OAuthHelper;
import org.flywaydb.core.Flyway;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = RoomPlannerApplication.class
)
@AutoConfigureMockMvc(secure = false)
@TestPropertySource(locations = "classpath:application-test.properties")
@EnableAutoConfiguration(exclude = SecurityAutoConfiguration.class)
@Sql(scripts = "classpath:scripts/clean-up.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class FloorRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Flyway flyway;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FloorRepository floorRepository;

    @Autowired
    private OAuthHelper oAuthHelper;

    private RequestPostProcessor bearerToken;

    @Before
    public void init() {
        flyway.clean();
        flyway.migrate();

        UserEntity userEntityPerson = BuildersWrapper.buildPersonEntiy(1L, "sghitun@yahoo.com", "sghitun", UserType.PERSON, "Stefania", "Ghitun");
        userRepository.save(userEntityPerson);

        bearerToken = oAuthHelper.addBearerToken("sghitun@yahoo.com", "person");

        UserEntity userEntityOne = BuildersWrapper.buildRoomEntity(2L, "wonderland@yahoo.com", "Wonderland",
                UserType.ROOM, "Wonderland", BuildersWrapper.buildFloorEntity(1L, 5), 14);
        UserEntity userEntityTwo = BuildersWrapper.buildRoomEntity(3L, "westeros@yahoo.com", "Westeros",
                UserType.ROOM, "Westeros", BuildersWrapper.buildFloorEntity(2L, 8), 20);
        UserEntity userEntityThree = BuildersWrapper.buildRoomEntity(4L, "neverland@yahoo.com", "Neverland",
                UserType.ROOM, "Neverland", BuildersWrapper.buildFloorEntity(3L, 4), 5);

        Set<RoomEntity> firstRoom = new HashSet<>();
        Set<RoomEntity> secondRoom = new HashSet<>();
        Set<RoomEntity> thirdRoom = new HashSet<>();

        firstRoom.add((RoomEntity) userEntityOne);
        secondRoom.add((RoomEntity) userEntityTwo);
        thirdRoom.add((RoomEntity) userEntityThree);

        FloorEntity floorEntityOne = BuildersWrapper.buildFloorEntityWithRooms(1L, 5, firstRoom);
        FloorEntity floorEntityTwo = BuildersWrapper.buildFloorEntityWithRooms(2L, 8, secondRoom);
        FloorEntity floorEntityThree = BuildersWrapper.buildFloorEntityWithRooms(3L, 4, thirdRoom);

        userRepository.save(userEntityOne);
        userRepository.save(userEntityTwo);
        userRepository.save(userEntityThree);

        floorRepository.save(floorEntityOne);
        floorRepository.save(floorEntityTwo);
        floorRepository.save(floorEntityThree);

    }

    @Test
    public void shouldReturnResponseEntityWithValidFloorDtoListAndStatusFoundWhenGetAllFloorsIsCalled() throws Exception {

        UserDto userEntityOne = BuildersWrapper.buildRoomDto(2L, "wonderland@yahoo.com", "Wonderland",
                null, 5, 14, UserType.ROOM);
        UserDto userEntityTwo = BuildersWrapper.buildRoomDto(3L, "westeros@yahoo.com", "Westeros",
                null, 8, 20, UserType.ROOM);

        UserDto userEntityThree = BuildersWrapper.buildRoomDto(4L, "neverland@yahoo.com", "Neverland",
                null, 4, 5, UserType.ROOM);

        Set<RoomDto> firstRoom = new HashSet<>();
        Set<RoomDto> secondRoom = new HashSet<>();
        Set<RoomDto> thirdRoom = new HashSet<>();

        firstRoom.add((RoomDto) userEntityOne);
        secondRoom.add((RoomDto) userEntityTwo);
        thirdRoom.add((RoomDto) userEntityThree);


        FloorDto floorDtoOne = BuildersWrapper.buildFloorDtoWithRooms(1L, 5, firstRoom);
        FloorDto floorDtoTwo = BuildersWrapper.buildFloorDtoWithRooms(2L, 8, secondRoom);
        FloorDto floorDtoThree = BuildersWrapper.buildFloorDtoWithRooms(3L, 4, thirdRoom);

        List<FloorDto> floorDtoList = Arrays.asList(floorDtoOne, floorDtoTwo, floorDtoThree);
        String jsonFloorDtoList = new ObjectMapper().writeValueAsString(floorDtoList);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/floors").with(bearerToken))
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.content().string(jsonFloorDtoList));

    }

}