package edu.roomplanner.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.roomplanner.RoomPlannerApplication;
import edu.roomplanner.dto.RoomDto;
import edu.roomplanner.entity.UserEntity;
import edu.roomplanner.repository.FloorRepository;
import edu.roomplanner.repository.ReservationRepository;
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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

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
    private FloorRepository floorRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @PersistenceContext
    private EntityManager entityManager;

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

        reservationRepository.deleteAll();
        userRepository.deleteAll();

        entityManager.createNativeQuery("ALTER SEQUENCE seq_user_id RESTART WITH 1").executeUpdate();
        entityManager.createNativeQuery("ALTER SEQUENCE seq_reservation_id RESTART WITH 1").executeUpdate();

        UserEntity userEntityPerson = BuildersWrapper.buildPersonEntity(1L, "sghitun@yahoo.com", "sghitun",
                new HashSet<>(), UserType.PERSON, "Stefania", "Ghitun");
        userRepository.save(userEntityPerson);

        bearerToken = oAuthHelper.addBearerToken("sghitun@yahoo.com", "person");
        roomBearerToken = oAuthHelper.addBearerToken("wonderland@yahoo.com", "room");
    }

    @Test
    public void shouldReturnResponseEntityWithValidRoomDtoListAndStatusFoundWhenGetAllRoomsIsCalled() throws Exception {
        UserEntity userEntityPerson = BuildersWrapper.buildPersonEntity(1L, "sghitun@yahoo.com", "sghitun", null, UserType.PERSON, "Stefania", "Ghitun");
        userRepository.save(userEntityPerson);

        UserEntity userEntityOne = BuildersWrapper.buildRoomEntity(2L, "wonderland@yahoo.com", "wonderland",
                new HashSet<>(), BuildersWrapper.buildFloorEntity(1L, 5), UserType.ROOM, "Wonderland", 14);
        UserEntity userEntityTwo = BuildersWrapper.buildRoomEntity(3L, "westeros@yahoo.com", "westeros",
                new HashSet<>(), BuildersWrapper.buildFloorEntity(2L, 8), UserType.ROOM, "Westeros", 20);
        UserEntity userEntityThree = BuildersWrapper.buildRoomEntity(4L, "neverland@yahoo.com", "neverland",
                new HashSet<>(), BuildersWrapper.buildFloorEntity(3L, 4), UserType.ROOM, "Neverland", 5);

        userRepository.save(userEntityOne);
        userRepository.save(userEntityTwo);
        userRepository.save(userEntityThree);

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

        UserEntity userEntityOne = BuildersWrapper.buildRoomEntity(2L, "wonderland@yahoo.com", "wonderland",
                new HashSet<>(), BuildersWrapper.buildFloorEntity(1L, 5), UserType.ROOM, "Wonderland", 14);

        userRepository.save(userEntityOne);

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

        mockMvc.perform(MockMvcRequestBuilders.get("/api/rooms/{id}", 1).with(bearerToken))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void shouldReturnResponseEntityWithStatusFoundWhenGetRoomsByFiltersIsCalledWithAllParameters() throws Exception {
        String startDate = "Fri, 28 Jun 2019 14:00:00 GMT";
        String endDate = "Fri, 28 Jun 2019 15:30:00 GMT";
        String minPersons = "7";
        String floor = "4";

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        params.add("startDate", startDate);
        params.add("endDate", endDate);
        params.add("minPersons", minPersons);
        params.add("floor", floor);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/rooms/filters")
                .params(params)
                .with(bearerToken))
                .andExpect(MockMvcResultMatchers.status().isFound());
    }

    @Test
    public void shouldReturnResponseEntityWithStatusFoundWhenGetRoomsByFiltersIsCalledWithNoFloorOrMinPersons() throws Exception {
        String startDate = "Fri, 28 Jun 2019 14:00:00 GMT";
        String endDate = "Fri, 28 Jun 2019 15:30:00 GMT";

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        params.add("startDate", startDate);
        params.add("endDate", endDate);


        mockMvc.perform(MockMvcRequestBuilders.get("/api/rooms/filters")
                .params(params)
                .with(bearerToken))
                .andExpect(MockMvcResultMatchers.status().isFound());
    }

    @Test
    public void shouldReturnResponseEntityWithStatusFoundAndEmptyListWhenGetRoomsByFiltersIsCalledWithValidFiltersButNoReservationsMatch() throws Exception {
        String startDate = "Fri, 28 Jun 2019 14:00:00 GMT";
        String endDate = "Fri, 28 Jun 2019 15:30:00 GMT";
        String minPersons = "7";
        String floor = "4";

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        params.add("startDate", startDate);
        params.add("endDate", endDate);
        params.add("minPersons", minPersons);
        params.add("floor", floor);


        mockMvc.perform(MockMvcRequestBuilders.get("/api/rooms/filters")
                .params(params)
                .with(bearerToken))
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    @Test
    public void shouldReturnResponseEntityWithStatusFoundAndValidWhenGetRoomsByFiltersIsCalledWithValidFiltersButNoReservationsMatch() throws Exception {

        UserEntity userEntityRoom = BuildersWrapper.buildRoomEntity(2L, "wonderland@yahoo.com", "wonderland",
                new HashSet<>(), BuildersWrapper.buildFloorEntity(1L, 5), UserType.ROOM, "Wonderland", 14);
        userRepository.save(userEntityRoom);


        RoomDto roomDto = BuildersWrapper.buildRoomDto(2L, "wonderland@yahoo.com", "Wonderland", Collections.EMPTY_SET, 5, 14, UserType.ROOM);
        List<RoomDto> expectedList = Collections.singletonList(roomDto);
        String jsonExpectedList = new ObjectMapper().writeValueAsString(expectedList);

        String startDateParam = "Fri, 26 Jul 2019 14:00:00 GMT";
        String endDateParam = "Fri, 26 Jul 2019 15:30:00 GMT";

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        params.add("startDate", startDateParam);
        params.add("endDate", endDateParam);


        mockMvc.perform(MockMvcRequestBuilders.get("/api/rooms/filters")
                .params(params)
                .with(bearerToken))
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.content().string(jsonExpectedList));
    }

    @Test
    public void shouldReturnResponseEntityWithSameRoomDtoAsTheLoggedOneWhenGetAllRoomsIsCalledByRoom() throws Exception {
        UserEntity userEntityRoom = BuildersWrapper.buildRoomEntity(2L, "wonderland@yahoo.com", "wonderland",
                new HashSet<>(), BuildersWrapper.buildFloorEntity(1L, 5), UserType.ROOM, "Wonderland", 14);
        userRepository.save(userEntityRoom);
        RoomDto roomDto = BuildersWrapper.buildRoomDto(2L, "wonderland@yahoo.com", "Wonderland",
                new HashSet<>(), 5, 14, UserType.ROOM);
        String jsonRoomDto = new ObjectMapper().writeValueAsString(Collections.singletonList(roomDto));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/rooms", 1).with(roomBearerToken))
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.content().string(jsonRoomDto));
    }

    @Test
    public void shouldReturnResponseEntityWithSameRoomDtoAsTheLoggedOneWhenGetRoomByIdIsCalledWithLoggedRoomId() throws Exception {
        UserEntity userEntityRoom = BuildersWrapper.buildRoomEntity(2L, "wonderland@yahoo.com", "wonderland",
                new HashSet<>(), BuildersWrapper.buildFloorEntity(1L, 5), UserType.ROOM, "Wonderland", 14);
        userRepository.save(userEntityRoom);
        RoomDto roomDto = BuildersWrapper.buildRoomDto(2L, "wonderland@yahoo.com", "Wonderland",
                new HashSet<>(), 5, 14, UserType.ROOM);
        String jsonRoomDto = new ObjectMapper().writeValueAsString(roomDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/rooms/2", 1).with(roomBearerToken))
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.content().string(jsonRoomDto));
    }

    @Test
    public void shouldReturnResponseEntityWithStatusUnauthorizedWhenGetRoomByIdIsCalledWithOtherIdThenLoggedRoomId() throws Exception {
        UserEntity userEntityWesteros = BuildersWrapper.buildRoomEntity(2L, "westeros@yahoo.com", "westeros",
                new HashSet<>(), BuildersWrapper.buildFloorEntity(2L, 8), UserType.ROOM, "Westeros", 20);
        UserEntity userEntityWonderland = BuildersWrapper.buildRoomEntity(3L, "wonderland@yahoo.com", "wonderland",
                new HashSet<>(), BuildersWrapper.buildFloorEntity(1L, 5), UserType.ROOM, "Wonderland", 14);
        userRepository.save(userEntityWesteros);
        userRepository.save(userEntityWonderland);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/rooms/2", 2).with(roomBearerToken))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    public void shouldReturnResponseEntityWithStatusUnauthorizedWhenGetUsersIsCalledByRoom() throws Exception {
        UserEntity userEntityWonderland = BuildersWrapper.buildRoomEntity(1L, "wonderland@yahoo.com", "wonderland",
                new HashSet<>(), BuildersWrapper.buildFloorEntity(1L, 5), UserType.ROOM, "Wonderland", 14);
        userRepository.save(userEntityWonderland);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/users?email=sghitun@yahoo.com", 1).with(roomBearerToken))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

}