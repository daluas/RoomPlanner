package edu.roomplanner.rooms;

import edu.roomplanner.entity.RoomEntity;
import edu.roomplanner.entity.UserEntity;
import edu.roomplanner.repository.UserRepository;
import edu.roomplanner.rest.UserRestController;
import edu.roomplanner.service.UserService;
import edu.roomplanner.service.ValidationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.validation.Validation;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(UserRestController.class)
public class UserControllerTests {

   /* @Autowired
    private MockMvc mvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserService userService;

    @MockBean
    private ValidationService validationService;


    @Test
    public void getAllRoomsAPI() throws Exception
    {
        RoomEntity roomEntity = RoomEntity.builder()
                .floor(1)
                .maxPersons(3)
                .roomName("Westeros")
                .build();
        roomEntity.setId(1L);
        userRepository.save(roomEntity);

        mvc.perform( MockMvcRequestBuilders
                .get("/rooms")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void getEmployeeByIdAPI() throws Exception
    {

        RoomEntity roomEntity = RoomEntity.builder()
                .floor(1)
                .maxPersons(3)
                .roomName("Westeros")
                .build();
        roomEntity.setId(1L);
        RoomEntity roomEntity2  = userRepository.save(roomEntity);

        UserEntity testUser = userRepository.findById(1L).get();

        mvc.perform( MockMvcRequestBuilders
                .get("/rooms/{id}", 1)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1));
    }*/
}
