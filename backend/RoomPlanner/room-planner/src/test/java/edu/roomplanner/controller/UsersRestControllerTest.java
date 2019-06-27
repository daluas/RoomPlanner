package edu.roomplanner.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.roomplanner.RoomPlannerApplication;
import edu.roomplanner.dto.RoomDto;
import edu.roomplanner.entity.UserEntity;
import edu.roomplanner.repository.UserRepository;
import edu.roomplanner.service.TokenParserService;
import edu.roomplanner.types.UserType;
import edu.roomplanner.util.BuildersWrapper;
import edu.roomplanner.util.OAuthHelper;
import org.flywaydb.core.Flyway;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
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

import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = RoomPlannerApplication.class
)
@AutoConfigureMockMvc(secure = false)
@TestPropertySource(locations = "classpath:application-test.properties")
@EnableAutoConfiguration(exclude = SecurityAutoConfiguration.class)
@Sql(scripts = "classpath:scripts/clean-up.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class UsersRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Flyway flyway;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OAuthHelper oAuthHelper;

    @Mock
    private TokenParserService tokenParserService;

    private RequestPostProcessor bearerToken;
    private RequestPostProcessor roomBearerToken;

    @Before
    public void init() {
        flyway.clean();
        flyway.migrate();

        UserEntity userEntityPerson = BuildersWrapper.buildPersonEntity(1L,"sghitun@yahoo.com","sghitun", null, UserType.PERSON,"Stefania","Ghitun");
        userRepository.save(userEntityPerson);

        bearerToken = oAuthHelper.addBearerToken("sghitun@yahoo.com", "person");
        roomBearerToken = oAuthHelper.addBearerToken("wonderland@yahoo.com", "room");

        UserEntity userEntityOne = BuildersWrapper.buildRoomEntity(2L, "wonderland@yahoo.com", "wonderland",
                new HashSet<>(), BuildersWrapper.buildFloorEntity(1L, 5), UserType.ROOM, "Wonderland", 14);
        UserEntity userEntityTwo = BuildersWrapper.buildRoomEntity(3L, "westeros@yahoo.com", "westeros",
                new HashSet<>(), BuildersWrapper.buildFloorEntity(2L, 8), UserType.ROOM, "Westeros",20);
        UserEntity userEntityThree = BuildersWrapper.buildRoomEntity(4L, "neverland@yahoo.com", "neverland",
                new HashSet<>(), BuildersWrapper.buildFloorEntity(3L, 4), UserType.ROOM, "Neverland", 5);

        userRepository.save(userEntityOne);
        userRepository.save(userEntityTwo);
        userRepository.save(userEntityThree);

    }

    @Test
    public void shouldReturnResponseEntityWithValidRoomDtoListAndStatusFoundWhenGetAllRoomsIsCalled() throws Exception {

        RoomDto roomDtoOne = BuildersWrapper.buildRoomDto(2L, "wonderland@yahoo.com", "Wonderland",
                new HashSet<>(), 5, 14, UserType.ROOM);
        RoomDto roomDtoTwo = BuildersWrapper.buildRoomDto(3L, "westeros@yahoo.com", "Westeros",
                new HashSet<>(), 8, 20, UserType.ROOM);
        RoomDto roomDtoThree = BuildersWrapper.buildRoomDto(4L, "neverland@yahoo.com", "Neverland",
                new HashSet<>(), 4, 5, UserType.ROOM);
        List<RoomDto> roomDtoList = Arrays.asList(roomDtoOne, roomDtoTwo, roomDtoThree);
        String jsonRoomDtoList = new ObjectMapper().writeValueAsString(roomDtoList);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/rooms").with(bearerToken))
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.content().string(jsonRoomDtoList));
    }

    @Test
    public void shouldReturnResponseEntityWithValidRoomDtoAndStatusFoundWhenGetRoomByIdIsCalled() throws Exception {

        RoomDto roomDto = BuildersWrapper.buildRoomDto(2L, "wonderland@yahoo.com", "Wonderland",
                new HashSet<>(), 5, 14, UserType.ROOM);
        String jsonRoomDto = new ObjectMapper().writeValueAsString(roomDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/rooms/{id}", 2).with(bearerToken))
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.content().string(jsonRoomDto));
    }

    @Test
    public void shouldReturnResponseEntityWithStatusNotFoundWhenGetRoomByIdIsCalledWithNonexistentID() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/rooms/{id}", 7).with(bearerToken))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void shouldReturnResponseEntityWithStatusNotFoundWhenGetRoomByIdIsCalledWithPersonID() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/rooms/{id}", 1).with(bearerToken))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void shouldReturnResponseEntityWithSameRoomDtoAsTheLoggedOneWhenGetAllRoomsIsCalledByRoom() throws Exception{
        RoomDto roomDto = BuildersWrapper.buildRoomDto(2L, "wonderland@yahoo.com", "Wonderland",
                new HashSet<>(), 5, 14, UserType.ROOM);
        String jsonRoomDto = new ObjectMapper().writeValueAsString(roomDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/rooms", 1).with(roomBearerToken))
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.content().string(jsonRoomDto));
    }

    @Test
    public void shouldReturnResponseEntityWithSameRoomDtoAsTheLoggedOneWhenGetRoomByIdIsCalledWithLoggedRoomId() throws Exception{
        RoomDto roomDto = BuildersWrapper.buildRoomDto(2L, "wonderland@yahoo.com", "Wonderland",
                new HashSet<>(), 5, 14, UserType.ROOM);
        String jsonRoomDto = new ObjectMapper().writeValueAsString(roomDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/rooms/2", 1).with(roomBearerToken))
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.content().string(jsonRoomDto));
    }

    @Test
    public void shouldReturnResponseEntityWithStatusUnauthorizedWhenGetRoomByIdIsCalledWith() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/rooms/{id}", 1).with(bearerToken))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
