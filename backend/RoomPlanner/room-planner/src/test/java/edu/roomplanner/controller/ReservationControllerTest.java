package edu.roomplanner.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.roomplanner.RoomPlannerApplication;
import edu.roomplanner.dto.ReservationDto;
import edu.roomplanner.entity.UserEntity;
import edu.roomplanner.entity.FloorEntity;
import edu.roomplanner.entity.ReservationEntity;
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
    @Autowired
    private ReservationDtoMapper mapperService;


    private RequestPostProcessor bearerTokenTest;

    @Before
    public void init() {
        flyway.clean();
        flyway.migrate();

        bearerToken = oAuthHelper.addBearerToken("sghitun@yahoo.com", "person");

        bearerTokenTest = oAuthHelper.addBearerToken("testEmail@yahoo.com", "person");

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

        UserEntity userEntityPerson = BuildersWrapper.buildPersonEntity(1L, "sghitun@yahoo.com", "sghitun",
                new HashSet<>(), UserType.PERSON, "Stefania", "Ghitun");
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
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

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
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

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

    @Test
    public void shouldReturnNotFoundWhenReservationIdIsInvalid() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/reservations?reservation=20")
                .with(bearerToken))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void shouldReturnNotFoundWhenReservationIsDeletedForAnotherUser() throws Exception {
        UserEntity userEntityPerson = BuildersWrapper.buildPersonEntity(1L, "sghitun@yahoo.com", "sghitun",
                new HashSet<>(), UserType.PERSON, "Stefania", "Ghitun");
        FloorEntity floorEntity = BuildersWrapper.buildFloorEntity(1L, 5);
        UserEntity roomEntity = BuildersWrapper.buildRoomEntity(3L, "wonderland@yahoo.com", "wonderland",
                new HashSet<>(), floorEntity, UserType.ROOM, "Wonderland", 4);
        UserEntity userEntityPersonOne = BuildersWrapper.buildPersonEntity(5L, "testEmail@yahoo.com", "test", null, UserType.PERSON, "Test", "Name");
        ReservationEntity reservationEntityTwo = BuildersWrapper.buildReservationEntity(2L, Calendar.getInstance(), Calendar.getInstance(), userEntityPerson,
                roomEntity, "ReservationToBeDeleted");
        reservationRepository.save(reservationEntityTwo);
        userRepository.save(userEntityPersonOne);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/reservations?reservation=2")
                .with(bearerTokenTest))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    public void shouldDeleteReservation() throws Exception {
        UserEntity userEntityPerson = BuildersWrapper.buildPersonEntity(1L, "sghitun@yahoo.com", "sghitun",
                new HashSet<>(), UserType.PERSON, "Stefania", "Ghitun");
        FloorEntity floorEntity = BuildersWrapper.buildFloorEntity(1L, 5);
        UserEntity roomEntity = BuildersWrapper.buildRoomEntity(3L, "wonderland@yahoo.com", "wonderland",
                new HashSet<>(), floorEntity, UserType.ROOM, "Wonderland", 4);
        UserEntity userEntityPersonOne = BuildersWrapper.buildPersonEntity(5L, "testEmail@yahoo.com", "test", null, UserType.PERSON, "Test", "Name");
        ReservationEntity reservationEntityTwo = BuildersWrapper.buildReservationEntity(2L, Calendar.getInstance(), Calendar.getInstance(), userEntityPerson,
                roomEntity, "ReservationToBeDeleted");
        reservationRepository.save(reservationEntityTwo);
        userRepository.save(userEntityPersonOne);
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/reservations?reservation=2")
                .with(bearerToken))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
    @Test
    public void shouldReturnResponseEntityWithValidReservationDtoAndStatusOkWhenUpdateReservationIsCalled() throws Exception {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.000Z");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        startDate.set(2019, Calendar.JULY, 7, 18, 10, 0);
        endDate.set(2019, Calendar.JULY, 7, 18, 45, 0);
        UserEntity roomEntity = BuildersWrapper.buildRoomEntity(2L, "wonderland@yahoo.com", "4wonD2C%",
                new HashSet<>(), new FloorEntity(), UserType.ROOM, "Wonderland", 14);
        UserEntity personEntity = BuildersWrapper.buildPersonEntity(1L, "sghitun@yahoo.com", "password",
                new HashSet<>(), UserType.PERSON, "Popescu", "Ana");
        ReservationEntity reservationEntity = BuildersWrapper.buildReservationEntity(1L, startDate, endDate, personEntity, roomEntity, "description");
        reservationRepository.save(reservationEntity);

        Calendar newStartDate = Calendar.getInstance();
        Calendar newEndDate = Calendar.getInstance();
        newStartDate.set(2040, Calendar.JUNE, 24, 13, 2, 0);
        newEndDate.set(2040, Calendar.JUNE, 24, 13, 33, 0);

        ReservationDto reservationDtoForUpdate = BuildersWrapper.buildReservationDto(null, null, null, newStartDate, newEndDate, "reservation updated");
        ReservationDto reservationDtoUpdated = BuildersWrapper.buildReservationDto(1L, 2L, "sghitun@yahoo.com", newStartDate, newEndDate, "reservation updated");
        String jsonReservationDto = new ObjectMapper().setDateFormat(dateFormat).writeValueAsString(reservationDtoForUpdate);
        String jsonUpdatedReservationDto = new ObjectMapper().setDateFormat(dateFormat).writeValueAsString(reservationDtoUpdated);

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/reservations/{id}", 1)
                .with(bearerToken)
                .content(jsonReservationDto)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().string(jsonUpdatedReservationDto))
                .andExpect(MockMvcResultMatchers.status().isOk());


    }

    @Test
    public void shouldReturnResponseEntityWithStatusNotFoundWhenUpdateReservationIsCalledWithInvalidId() throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.000Z");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        startDate.set(2035, Calendar.JANUARY, 9, 12, 10, 0);
        endDate.set(2035, Calendar.JANUARY, 6, 13, 45, 0);
        ReservationDto reservationDto = BuildersWrapper.buildReservationDto(null, null, null, startDate, endDate, "reservation updated");
        String jsonReservationDto = new ObjectMapper().setDateFormat(dateFormat).writeValueAsString(reservationDto);
        mockMvc.perform(MockMvcRequestBuilders.patch("/api/reservations/{id}", 407)
                .with(bearerToken)
                .content(jsonReservationDto)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}