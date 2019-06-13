package edu.roomplanner.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.roomplanner.RoomPlannerApplication;
import edu.roomplanner.dto.RoomDto;
import edu.roomplanner.entity.UserEntity;
import edu.roomplanner.repository.UserRepository;
import edu.roomplanner.types.UserType;
import edu.roomplanner.builders.RoomDtoBuilder;
import edu.roomplanner.builders.RoomEntityBuilder;
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
public class UsersRestControllerTests {

    @Autowired
    private MockMvc mockMvc;

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
    public void shouldReturnResponseEntityWithValidRoomDtoListAndStatusFoundWhenGetAllRoomsIsCalled() throws Exception {

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

        List<RoomDto> roomDtoList = Arrays.asList(roomDtoOne, roomDtoTwo);
        String jsonRoomDtoList = new ObjectMapper().writeValueAsString(roomDtoList);

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

        mockMvc.perform(MockMvcRequestBuilders.get("/rooms"))
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.content().string(jsonRoomDtoList));
    }

    @Test
    public void shouldReturnResponseEntityWithValidRoomDtoAndStatusFoundWhenGetRoomByIdIsCalled() throws Exception {

        RoomDto roomDto = new RoomDtoBuilder()
                .withId(1L)
                .withName("Wonderland")
                .withFloor(5)
                .withMaxPersons(14)
                .build();
        String jsonRoomDto = new ObjectMapper().writeValueAsString(roomDto);

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
