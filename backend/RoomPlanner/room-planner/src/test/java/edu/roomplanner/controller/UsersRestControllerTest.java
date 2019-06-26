package edu.roomplanner.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.roomplanner.RoomPlannerApplication;
import edu.roomplanner.dto.ReservationDto;
import edu.roomplanner.dto.RoomDto;
import edu.roomplanner.entity.ReservationEntity;
import edu.roomplanner.entity.UserEntity;
import edu.roomplanner.repository.ReservationRepository;
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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = RoomPlannerApplication.class
)
@Transactional
@AutoConfigureMockMvc(secure = false)
@TestPropertySource(locations = "classpath:application-test.properties")
@EnableAutoConfiguration(exclude = SecurityAutoConfiguration.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UsersRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Flyway flyway;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private OAuthHelper oAuthHelper;

    private RequestPostProcessor bearerToken;

    @Before
    public void init() {
        flyway.clean();
        flyway.migrate();

       reservationRepository.deleteAll();
        userRepository.deleteAll();

        entityManager.createNativeQuery("ALTER SEQUENCE seq_user_id RESTART WITH 1").executeUpdate();
        entityManager.createNativeQuery("ALTER SEQUENCE seq_reservation_id RESTART WITH 1").executeUpdate();

        UserEntity userEntityPerson = BuildersWrapper.buildPersonEntiy(1L,"sghitun@yahoo.com","sghitun",UserType.PERSON,"Stefania","Ghitun");
        userRepository.save(userEntityPerson);

        bearerToken = oAuthHelper.addBearerToken("sghitun@yahoo.com", "person");
    }

    @Test
    public void shouldReturnResponseEntityWithValidRoomDtoListAndStatusFoundWhenGetAllRoomsIsCalled() throws Exception {

        UserEntity userEntityOne = BuildersWrapper.buildRoomEntity(2L, "wonderland@yahoo.com", "wonderland",
                UserType.ROOM, "Wonderland", 5, 14);
        UserEntity userEntityTwo = BuildersWrapper.buildRoomEntity(3L, "westeros@yahoo.com", "westeros",
                UserType.ROOM, "Westeros", 8, 20);
        UserEntity userEntityThree = BuildersWrapper.buildRoomEntity(4L, "neverland@yahoo.com", "neverland",
                UserType.ROOM, "Neverland", 4, 5);

        userRepository.save(userEntityOne);
        userRepository.save(userEntityTwo);
        userRepository.save(userEntityThree);

        RoomDto roomDtoOne = BuildersWrapper.buildRoomDto(2L, "wonderland@yahoo.com", "Wonderland",  Collections.EMPTY_SET, 5, 14, UserType.ROOM);
        RoomDto roomDtoTwo = BuildersWrapper.buildRoomDto(3L, "westeros@yahoo.com", "Westeros",  Collections.EMPTY_SET, 8, 20, UserType.ROOM);
        RoomDto roomDtoThree = BuildersWrapper.buildRoomDto(4L, "neverland@yahoo.com", "Neverland", Collections.EMPTY_SET, 4, 5, UserType.ROOM);
        List<RoomDto> roomDtoList = Arrays.asList(roomDtoOne, roomDtoTwo, roomDtoThree);
        String jsonRoomDtoList = new ObjectMapper().writeValueAsString(roomDtoList);

        mockMvc.perform(MockMvcRequestBuilders.get("/rooms").with(bearerToken))
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.content().string(jsonRoomDtoList));
    }

    @Test
    public void shouldReturnResponseEntityWithValidRoomDtoAndStatusFoundWhenGetRoomByIdIsCalled() throws Exception {

        UserEntity userEntityOne = BuildersWrapper.buildRoomEntity(2L, "wonderland@yahoo.com", "wonderland",
                UserType.ROOM, "Wonderland", 5, 14);

        userRepository.save(userEntityOne);

        RoomDto roomDto = BuildersWrapper.buildRoomDto(2L, "wonderland@yahoo.com", "Wonderland",  Collections.EMPTY_SET, 5, 14, UserType.ROOM);
        String jsonRoomDto = new ObjectMapper().writeValueAsString(roomDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/rooms/{id}", 2).with(bearerToken))
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.content().string(jsonRoomDto));
    }

    @Test
    public void shouldReturnResponseEntityWithStatusNotFoundWhenGetRoomByIdIsCalledWithNonexistentID() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/rooms/{id}", 7).with(bearerToken))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void shouldReturnResponseEntityWithStatusNotFoundWhenGetRoomByIdIsCalledWithPersonID() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/rooms/{id}", 1).with(bearerToken))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void shouldReturnResponseEntityWithStatusFoundWhenGetRoomsByFiltersIsCalledWithAllParameters() throws Exception {
        String startDate = "Fri, 28 Jun 2019 14:00:00 GMT";
        String endDate ="Fri, 28 Jun 2019 15:30:00 GMT";
        String minPersons = "7";
        String floor = "4";

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        params.add("startDate",startDate);
        params.add("endDate",endDate);
        params.add("minPersons",minPersons);
        params.add("floor",floor);

        mockMvc.perform(MockMvcRequestBuilders.get("/rooms/filters")
                .params(params)
                .with(bearerToken))
                .andExpect(MockMvcResultMatchers.status().isFound());
    }

    @Test
    public void shouldReturnResponseEntityWithStatusFoundWhenGetRoomsByFiltersIsCalledWithNoFloorOrMinPersons() throws Exception {
        String startDate = "Fri, 28 Jun 2019 14:00:00 GMT";
        String endDate ="Fri, 28 Jun 2019 15:30:00 GMT";

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        params.add("startDate",startDate);
        params.add("endDate",endDate);


        mockMvc.perform(MockMvcRequestBuilders.get("/rooms/filters")
                .params(params)
                .with(bearerToken))
                .andExpect(MockMvcResultMatchers.status().isFound());
    }

    @Test
    public void shouldReturnResponseEntityWithStatusFoundAndEmptyListWhenGetRoomsByFiltersIsCalledWithValidFiltersButNoReservationsMatch() throws Exception {
        String startDate = "Fri, 28 Jun 2019 14:00:00 GMT";
        String endDate ="Fri, 28 Jun 2019 15:30:00 GMT";
        String minPersons = "7";
        String floor = "4";

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        params.add("startDate",startDate);
        params.add("endDate",endDate);
        params.add("minPersons",minPersons);
        params.add("floor",floor);


        mockMvc.perform(MockMvcRequestBuilders.get("/rooms/filters")
                .params(params)
                .with(bearerToken))
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    @Test
    public void shouldReturnResponseEntityWithStatusFoundAndValidWhenGetRoomsByFiltersIsCalledWithValidFiltersButNoReservationsMatch() throws Exception {

       /* UserEntity userEntityPerson = BuildersWrapper.buildPersonEntiy(1L,"sghitun@yahoo.com","sghitun",
                UserType.PERSON,"Stefania","Ghitun");
        userRepository.save(userEntityPerson);*/
        UserEntity userEntityRoom = BuildersWrapper.buildRoomEntity(2L, "wonderland@yahoo.com", "wonderland",
                UserType.ROOM, "Wonderland", 5, 14);
        userRepository.save(userEntityRoom);

       /* Calendar startDate = Calendar.getInstance();
        startDate.set(2019,Calendar.JUNE,28,10,0,0);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2019,Calendar.JUNE,28,11,30,0);

        ReservationEntity reservationEntity = BuildersWrapper.buildReservationEntity(1L,startDate,endDate,"Demo meeting",userEntityPerson,userEntityRoom);
        reservationRepository.save(reservationEntity);

        ReservationDto expectedReservation = BuildersWrapper.buildReservationDto(1L,2L,"sghitun@yahoo.com",startDate,endDate,"Demo meeting");*/

        RoomDto roomDto = BuildersWrapper.buildRoomDto(2L, "wonderland@yahoo.com", "Wonderland",  Collections.EMPTY_SET, 5, 14, UserType.ROOM);
        List<RoomDto> expectedList = Arrays.asList(roomDto);
        String jsonExpectedList = new ObjectMapper().writeValueAsString(expectedList);

        String startDateParam = "Fri, 28 Jun 2019 14:00:00 GMT";
        String endDateParam ="Fri, 28 Jun 2019 15:30:00 GMT";

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        params.add("startDate",startDateParam);
        params.add("endDate",endDateParam);


        mockMvc.perform(MockMvcRequestBuilders.get("/rooms/filters")
                .params(params)
                .with(bearerToken))
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.content().string(jsonExpectedList));
    }
}
