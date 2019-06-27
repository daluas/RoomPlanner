package edu.roomplanner.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.roomplanner.RoomPlannerApplication;
import edu.roomplanner.dto.RoomDto;
import edu.roomplanner.entity.UserEntity;
import edu.roomplanner.repository.UserRepository;
import edu.roomplanner.types.UserType;
import edu.roomplanner.util.BuildersWrapper;
import edu.roomplanner.util.OAuthHelper1;
import org.flywaydb.core.Flyway;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = RoomPlannerApplication.class
)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
public class UsersRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Flyway flyway;

    @Autowired
    private OAuthHelper1 oAuthHelper;

    private RequestPostProcessor bearerToken;

    @Before
    public void init() {
        flyway.clean();
        flyway.migrate();

        bearerToken = oAuthHelper.addBearerToken("sghitun@yahoo.com", "person");
    }

    @Autowired
    private UserRepository userRepository;

    @Test
    public void shouldReturnResponseEntityWithValidRoomDtoListAndStatusFoundWhenGetAllRoomsIsCalled() throws Exception {

        RoomDto roomDtoOne = BuildersWrapper.buildRoomDto(2L, "Wonderland", 5, 14);
        RoomDto roomDtoTwo = BuildersWrapper.buildRoomDto(3L, "Westeros", 8, 20);
        RoomDto roomDtoThree = BuildersWrapper.buildRoomDto(4L, "Neverland", 4, 5);
        List<RoomDto> roomDtoList = Arrays.asList(roomDtoOne, roomDtoTwo, roomDtoThree);
        String jsonRoomDtoList = new ObjectMapper().writeValueAsString(roomDtoList);


        mockMvc.perform(MockMvcRequestBuilders.get("/rooms").with(bearerToken))
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.content().string(jsonRoomDtoList));
    }

    @Test
    public void shouldReturnResponseEntityWithValidRoomDtoAndStatusFoundWhenGetRoomByIdIsCalled() throws Exception {

        RoomDto roomDto = BuildersWrapper.buildRoomDto(1L, "Wonderland", 5, 14);
        String jsonRoomDto = new ObjectMapper().writeValueAsString(roomDto);

        UserEntity userEntity = BuildersWrapper.buildRoomEntity(1L, "wonderland@yahoo.com", "4wonD2C%",
                UserType.ROOM, "Wonderland", 5, 14);
        userRepository.save(userEntity);

        mockMvc.perform(MockMvcRequestBuilders.get("/rooms/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.content().string(jsonRoomDto));
    }

    @Test
    public void shouldReturnResponseEntityWithStatusNotFoundWhenGetRoomByIdIsCalledWithNonexistentID() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/rooms/{id}", 7))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void shouldReturnResponseEntityWithStatusNotFoundWhenGetRoomByIdIsCalledWithPersonID() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/rooms/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
