package edu.roomplanner.rooms;

import edu.roomplanner.rest.UserRestService;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@RunWith(SpringRunner.class)
@WebMvcTest(UserRestService.class)
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
