package edu.roomplanner.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.roomplanner.RoomPlannerApplication;
import edu.roomplanner.dto.RoomDto;
import edu.roomplanner.utils.BuilderClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
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
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:scripts/init_h2db.sql")
@TestPropertySource(locations = "classpath:application-test.properties")
public class UsersRestControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void Should_Return_ResponseEntity_With_Valid_RoomDto_List_And_Status_Found_When_GetAllRooms_Is_Called() throws Exception {

        RoomDto testRoomDto1 = BuilderClass.buildRoomDto(2L, "Wonderland", 5, 14);
        RoomDto testRoomDto2 = BuilderClass.buildRoomDto(3L, "Westeros", 8, 21);

        List<RoomDto> roomDtoList = Arrays.asList(testRoomDto1, testRoomDto2);
        String jsonRoomDtoList = new ObjectMapper().writeValueAsString(roomDtoList);

        mockMvc.perform(MockMvcRequestBuilders.get("/rooms"))
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.content().string(jsonRoomDtoList));
    }

    @Test
    public void Should_Return_ResponseEntity_With_Valid_RoomDto_And_Status_Found_When_GetRoomById_Is_Called() throws Exception {

        RoomDto testRoomDto = BuilderClass.buildRoomDto(2L, "Wonderland", 5, 14);
        String jsonRoomDto = new ObjectMapper().writeValueAsString(testRoomDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/rooms/{id}", 2))
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.content().string(jsonRoomDto));
    }

    @Test
    public void Should_Return_ResponseEntity_With_Status_NotFound_When_GetRoomById_IsCalled_With_Nonexistent_ID() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/rooms/{id}", 7))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void Should_Return_ResponseEntity_With_Status_NotFound_When_GetRoomById_IsCalled_With_Person_ID() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/rooms/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
