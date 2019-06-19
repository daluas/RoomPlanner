package edu.roomplanner.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.roomplanner.RoomPlannerApplication;
import edu.roomplanner.dto.RoomDto;
import edu.roomplanner.entity.UserEntity;
import edu.roomplanner.repository.UserRepository;
import edu.roomplanner.types.UserType;
import edu.roomplanner.util.BuildersWrapper;
import org.flywaydb.core.Flyway;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

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

    @Before
    public void init() {
        flyway.clean();
        flyway.migrate();
    }

    @Autowired
    private UserRepository userRepository;
    private String loginContent;

    @Before
    public void setup() throws Exception{

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "password");
        params.add("username", "wonderland@yahoo.com");
        params.add("password", "wonderland");

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("oauth/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
               .header("Authorization", "Basic YnJvd3NlcjpwaW4=")
                .params(params)).andReturn();
        loginContent = result.getResponse().getContentAsString();
    }

    @Test
    public void shouldReturnResponseEntityWithValidRoomDtoListAndStatusFoundWhenGetAllRoomsIsCalled() throws Exception {

        RoomDto roomDtoOne = BuildersWrapper.buildRoomDto(1L, "Wonderland", 5, 14, UserType.ROOM);
        RoomDto roomDtoTwo = BuildersWrapper.buildRoomDto(2L, "Westeros", 8, 21, UserType.ROOM);
        List<RoomDto> roomDtoList = Arrays.asList(roomDtoOne, roomDtoTwo);
        String jsonRoomDtoList = new ObjectMapper().writeValueAsString(roomDtoList);

        UserEntity userEntityOne = BuildersWrapper.buildRoomEntity(1L, "wonderland@yahoo.com", "wonderland",
                UserType.ROOM, "Wonderland", 5, 14);
        UserEntity userEntityTwo = BuildersWrapper.buildRoomEntity(2L, "westeros@yahoo.com", "westeros",
                UserType.ROOM, "Westeros", 8, 21);

        userRepository.save(userEntityOne);
        userRepository.save(userEntityTwo);

       //mockMvc.perform(MockMvcRequestBuilders.get("/rooms").header("Authorization", ))
              //  .andExpect(MockMvcResultMatchers.status().isFound())
              //  .andExpect(MockMvcResultMatchers.content().string(jsonRoomDtoList));
    }

    @Test
    public void shouldReturnResponseEntityWithValidRoomDtoAndStatusFoundWhenGetRoomByIdIsCalled() throws Exception {


        RoomDto roomDto = BuildersWrapper.buildRoomDto(2L, "Wonderland", 5, 14, UserType.ROOM);

        String jsonRoomDto = new ObjectMapper().writeValueAsString(roomDto);

       /* UserEntity userEntity = BuildersWrapper.buildRoomEntity(1L, "wonderland@yahoo.com", "wonderland",
                UserType.ROOM, "Wonderland", 5, 14);
        userRepository.save(userEntity);*/

        mockMvc.perform(MockMvcRequestBuilders.get("/rooms/{id}", 2).header("Authorization",loginContent))
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
