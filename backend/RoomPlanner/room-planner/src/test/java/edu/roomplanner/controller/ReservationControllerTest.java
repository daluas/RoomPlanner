package edu.roomplanner.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.roomplanner.RoomPlannerApplication;
import edu.roomplanner.dto.ReservationDto;
import edu.roomplanner.entity.*;
import edu.roomplanner.mappers.ReservationDtoMapper;
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
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.TimeZone;

@RunWith(SpringRunner.class)
@Transactional
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = RoomPlannerApplication.class
)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ReservationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Flyway flyway;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReservationDtoMapper reservationDtoMapper;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private OAuthHelper oAuthHelper;

    private RequestPostProcessor bearerToken;

    @Before
    public void init() {
        flyway.clean();
        flyway.migrate();

        bearerToken = oAuthHelper.addBearerToken("sghitun@yahoo.com", "person");

        Calendar startDate = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        Calendar endDate = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        startDate.set(2019, Calendar.AUGUST, 6, 7, 10, 0);
        startDate.set(Calendar.MILLISECOND, 0);
        endDate.set(2019, Calendar.AUGUST, 6, 7, 45, 0);
        endDate.set(Calendar.MILLISECOND, 0);

        reservationRepository.deleteAll();
        userRepository.deleteAll();

        entityManager.createNativeQuery("ALTER SEQUENCE seq_user_id RESTART WITH 1").executeUpdate();
        entityManager.createNativeQuery("ALTER SEQUENCE seq_reservation_id RESTART WITH 1").executeUpdate();

        UserEntity userEntityPerson = BuildersWrapper.buildPersonEntity(1L,"sghitun@yahoo.com","sghitun",
                new HashSet<>(), UserType.PERSON,"Stefania","Ghitun");
        userRepository.save(userEntityPerson);

        UserEntity personEntity = BuildersWrapper.buildPersonEntity(2L, "marius@yahoo.com", "marius",
                new HashSet<>(), UserType.PERSON, "parasca", "marius");
        userRepository.save(personEntity);

        FloorEntity floorEntity = BuildersWrapper.buildFloorEntity(1L, 5);

        UserEntity roomEntity = BuildersWrapper.buildRoomEntity(3L, "wonderland@yahoo.com", "wonderland",
                new HashSet<>(), floorEntity, UserType.ROOM, "Wonderland", 4);
        userRepository.save(roomEntity);
        ReservationEntity reservationEntity = BuildersWrapper.buildReservationEntity(1L, startDate, endDate, personEntity,
                roomEntity, "ok");
        reservationRepository.save(reservationEntity);
    }

    @Test
    public void shouldReturnResponseEntityWithValidReservationDtoAndStatusCreatedWhenPostReservationCreatedIsCalled() throws Exception {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.000Z");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        startDate.set(2019, Calendar.AUGUST, 6, 10, 10, 0);
        endDate.set(2019, Calendar.AUGUST, 6, 10, 45, 0);
        ReservationDto reservationDto = BuildersWrapper.buildReservationDto(1L, 3L, "marius@yahoo.com", startDate, endDate, "description");
        String jsonReservationDto = new ObjectMapper().setDateFormat(dateFormat).writeValueAsString(reservationDto);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/reservations/{room_id}", 3)
                .with(bearerToken)
                .content(jsonReservationDto)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().string(jsonReservationDto));

    }

    @Test
    public void shouldReturnResponseEntityWithStatusNotFoundWhenPostReservationCreatedIsCalledWithNonexistentPerson() throws Exception {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.000Z");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        startDate.set(2019, Calendar.JANUARY, 6, 10, 10, 0);
        endDate.set(2019, Calendar.JANUARY, 6, 10, 45, 0);
        ReservationDto reservationDto = BuildersWrapper.buildReservationDto(1L, 2L, "email@yahoo.com", startDate, endDate, "description");
        String jsonReservationDto = new ObjectMapper().setDateFormat(dateFormat).writeValueAsString(reservationDto);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/reservations/{room_id}", 2)
                .with(bearerToken)
                .content(jsonReservationDto)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());

    }

    @Test
    public void shouldReturnResponseEntityWithStatusNotFoundWhenPostReservationCreatedIsCalledWithNonexistentRoom() throws Exception {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.000Z");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        startDate.set(2019, Calendar.JANUARY, 6, 10, 10, 0);
        endDate.set(2019, Calendar.JANUARY, 6, 10, 45, 0);
        ReservationDto reservationDto = BuildersWrapper.buildReservationDto(1L, 2L, "sghitun@yahoo.com", startDate, endDate, "description");
        String jsonReservationDto = new ObjectMapper().setDateFormat(dateFormat).writeValueAsString(reservationDto);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/reservations/{room_id}", 255)
                .with(bearerToken)
                .content(jsonReservationDto)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());

    }


    @Test
    public void shouldReturnBadRequestWhenPostReservationThatIsAlreadyBooked() throws Exception {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.000Z");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        startDate.set(2019, Calendar.AUGUST, 6, 10, 10, 0);
        startDate.set(Calendar.MILLISECOND, 0);
        endDate.set(2019, Calendar.AUGUST, 6, 10, 45, 0);
        endDate.set(Calendar.MILLISECOND, 0);
        ReservationDto reservationDto = BuildersWrapper.buildReservationDto(1L, null, "sghitun@yahoo.com",
                startDate, endDate, "description");

        String jsonReservationDto = new ObjectMapper().setDateFormat(dateFormat).writeValueAsString(reservationDto);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/reservations/{room_id}", 3)
                .with(bearerToken)
                .content(jsonReservationDto)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

}