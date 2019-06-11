package edu.roomplanner.rooms;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.roomplanner.entity.RoomEntity;
import edu.roomplanner.entity.UserEntity;
import edu.roomplanner.repository.UserRepository;
import edu.roomplanner.service.UserService;
import edu.roomplanner.service.ValidationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.persistence.EntityManager;
import java.util.Collections;


@RunWith(SpringRunner.class)
/*@SpringBootTest(
       webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,

)*/
public class UsersRestControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EntityManager entityManager;

    private UserService userService;

    private ValidationService validationService;

    private UserRepository userRepository;

    @Test
    public void registrationWorksThroughAllLayers() throws Exception {


        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/rooms")
                        .accept(MediaType.APPLICATION_JSON)
        ).andReturn();

        System.out.println(result.getResponse());

        Mockito.verify(userRepository).findByType("room");
    }
}
