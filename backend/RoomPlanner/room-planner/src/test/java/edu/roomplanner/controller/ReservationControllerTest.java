package edu.roomplanner.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.roomplanner.RoomPlannerApplication;
import edu.roomplanner.dto.ReservationDto;
import edu.roomplanner.entity.UserEntity;
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
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = RoomPlannerApplication.class
)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
public class ReservationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Flyway flyway;

    @Autowired
    private OAuthHelper oAuthHelper;

    @Autowired
    private UserRepository userRepository;

    private RequestPostProcessor bearerToken;

    private RequestPostProcessor bearerTokenTest;

    @Before
    public void init() {
        flyway.clean();
        flyway.migrate();

        UserEntity userEntityPerson = BuildersWrapper.buildPersonEntity(5L, "testEmail@yahoo.com", "test", null, UserType.PERSON, "Test", "Name");
        userRepository.save(userEntityPerson);

        bearerToken = oAuthHelper.addBearerToken("sghitun@yahoo.com", "person");

        bearerTokenTest = oAuthHelper.addBearerToken("testEmail@yahoo.com", "person");
    }

    @Test
    public void shouldReturnResponseEntityWithValidReservationDtoAndStatusCreatedWhenPostReservationCreatedIsCalled() throws Exception {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.000Z");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        startDate.set(2019, Calendar.JANUARY, 6, 10, 10, 0);
        endDate.set(2019, Calendar.JANUARY, 6, 10, 45, 0);
        ReservationDto reservationDto = BuildersWrapper.buildReservationDto(1L, 2L, "sghitun@yahoo.com", startDate, endDate, "description");
        String jsonReservationDto = new ObjectMapper().setDateFormat(dateFormat).writeValueAsString(reservationDto);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/reservations/{room_id}", 2)
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
    public void shouldReturnNotFoundWhenReservationIdIsInvalid() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/reservations?reservation=20")
                .with(bearerToken))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void shouldReturnNotFoundWhenReservationIsDeletedForAnotherUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/reservations?reservation=11")
                .with(bearerTokenTest))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void shouldDeleteReservation() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/reservations?reservation=11")
                .with(bearerToken))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}