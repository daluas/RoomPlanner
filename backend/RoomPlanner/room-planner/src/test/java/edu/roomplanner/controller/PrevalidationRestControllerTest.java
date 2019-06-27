package edu.roomplanner.controller;

import edu.roomplanner.RoomPlannerApplication;
import edu.roomplanner.builders.ReservationEntityBuilder;
import edu.roomplanner.entity.ReservationEntity;
import edu.roomplanner.entity.UserEntity;
import edu.roomplanner.repository.ReservationRepository;
import edu.roomplanner.repository.UserRepository;
import edu.roomplanner.types.UserType;
import edu.roomplanner.util.BuildersWrapper;
import edu.roomplanner.util.OAuthHelper;
import edu.roomplanner.util.ReservationEntityUtil;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashSet;
import java.util.TimeZone;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = RoomPlannerApplication.class
)
@AutoConfigureMockMvc(secure = false)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@TestPropertySource(locations = "classpath:application-test.properties")
@EnableAutoConfiguration(exclude = SecurityAutoConfiguration.class)
@Sql(scripts = "classpath:scripts/clean-up.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class PrevalidationRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Flyway flyway;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private OAuthHelper oAuthHelper;

    private RequestPostProcessor bearerToken;

    private DateFormat df = new SimpleDateFormat("EEE', 'dd' 'MMM' 'yyyy' 'hh:mm:ss' GMT'");

    @Before
    public void init() {
        flyway.clean();
        flyway.migrate();

        df.setTimeZone(TimeZone.getTimeZone("GMT"));

        UserEntity userEntityPersonOne = BuildersWrapper.buildPersonEntity(1L, "sghitun@yahoo.com", "sghitun", null, UserType.PERSON, "Stefania", "Ghitun");
        UserEntity userEntityPersonTwo = BuildersWrapper.buildPersonEntity(5L, "testEmail@yahoo.com", "test", null, UserType.PERSON, "Test", "Name");
        userRepository.save(userEntityPersonOne);
        userRepository.save(userEntityPersonTwo);

        bearerToken = oAuthHelper.addBearerToken("sghitun@yahoo.com", "person");

        UserEntity userEntityOne = BuildersWrapper.buildRoomEntity(2L, "wonderland@yahoo.com", "wonderland",
                new HashSet<>(), BuildersWrapper.buildFloorEntity(1L, 5), UserType.ROOM, "Wonderland", 14);
        UserEntity userEntityTwo = BuildersWrapper.buildRoomEntity(3L, "westeros@yahoo.com", "westeros",
                new HashSet<>(), BuildersWrapper.buildFloorEntity(2L, 8), UserType.ROOM, "Westeros", 20);
        UserEntity userEntityThree = BuildersWrapper.buildRoomEntity(4L, "neverland@yahoo.com", "neverland",
                new HashSet<>(), BuildersWrapper.buildFloorEntity(3L, 4), UserType.ROOM, "Neverland", 5);

        Calendar sysDate = Calendar.getInstance();
        sysDate.setTimeZone(TimeZone.getTimeZone("GMT"));

        ReservationEntityUtil reservationEntityUtil = new ReservationEntityUtil();
        Calendar startDate = reservationEntityUtil.createDate(sysDate.get(Calendar.YEAR), sysDate.get(Calendar.MONTH), sysDate.get(Calendar.DAY_OF_MONTH),
                sysDate.get(Calendar.HOUR), sysDate.get(Calendar.MINUTE), 0);
        Calendar endDate = reservationEntityUtil.createDate(sysDate.get(Calendar.YEAR), sysDate.get(Calendar.MONTH), sysDate.get(Calendar.DAY_OF_MONTH),
                sysDate.get(Calendar.HOUR), sysDate.get(Calendar.MINUTE) + 31, 0);

        ReservationEntity reservationEntityOne = new ReservationEntityBuilder()
                .withStartDate(startDate)
                .withEndDate(endDate)
                .withPerson(userEntityPersonOne)
                .withRoom(userEntityOne).build();

        startDate = reservationEntityUtil.createDate(sysDate.get(Calendar.YEAR), sysDate.get(Calendar.MONTH), sysDate.get(Calendar.DAY_OF_MONTH),
                sysDate.get(Calendar.HOUR) + 2, sysDate.get(Calendar.MINUTE), 0);
        endDate = reservationEntityUtil.createDate(sysDate.get(Calendar.YEAR), sysDate.get(Calendar.MONTH), sysDate.get(Calendar.DAY_OF_MONTH),
                sysDate.get(Calendar.HOUR) + 2, sysDate.get(Calendar.MINUTE) + 31, 0);

        ReservationEntity reservationEntityTwo = new ReservationEntityBuilder()
                .withStartDate(startDate)
                .withEndDate(endDate)
                .withPerson(userEntityPersonOne)
                .withRoom(userEntityOne).build();

        reservationRepository.save(reservationEntityOne);
        reservationRepository.save(reservationEntityTwo);

        userRepository.save(userEntityOne);
        userRepository.save(userEntityTwo);
        userRepository.save(userEntityThree);

    }

    @Test
    public void shouldReturnInvalidParametersWhenCalledWithPastDate() throws Exception {

        Integer result = 400;

        mockMvc.perform(MockMvcRequestBuilders.get("/api/prevalidation?roomId=2&startDate=Thu, 27 Jun 2019 5:53:00 GMT&endDate=Thu, 27 Jun 2019 8:30:00 GMT&email=sghitun@yahoo.com")
                .with(bearerToken))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    @Test
    public void shouldReturnInvalidParametersWhenCalledWithWrongEmail() throws Exception {

        Integer result = 400;

        Calendar startDate = Calendar.getInstance();
        startDate.add(startDate.MINUTE, 10);

        Calendar endDate = Calendar.getInstance();
        endDate.add(startDate.MINUTE, 40);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/prevalidation?roomId=2&startDate=" + df.format(startDate.getTime()) + "&endDate=" + df.format(endDate.getTime()) + "&email=notExistEmail@yahoo.com")
                .with(bearerToken))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    @Test
    public void shouldReturnInvalidParametersWhenCalledWithWrongRoomId() throws Exception {

        Integer result = 400;

        Calendar startDate = Calendar.getInstance();
        startDate.add(startDate.MINUTE, 10);

        Calendar endDate = Calendar.getInstance();
        endDate.set(Calendar.MINUTE, startDate.get(Calendar.MINUTE));
        endDate.add(endDate.MINUTE, 40);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/prevalidation?roomId=5000&startDate=" + df.format(startDate.getTime()) + "&endDate=" + df.format(endDate.getTime()) + "&email=sghitun@yahoo.com")
                .with(bearerToken))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void shouldReturnInvalidParametersWhenCalledWithLessThanMinMinutes() throws Exception {

        Integer result = 400;

        Calendar startDate = Calendar.getInstance();
        startDate.add(startDate.MINUTE, 10);

        Calendar endDate = Calendar.getInstance();
        endDate.set(Calendar.MINUTE, startDate.get(Calendar.MINUTE));
        endDate.add(endDate.MINUTE, 20);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/prevalidation?roomId=2&startDate=" + df.format(startDate.getTime()) + "&endDate=" + df.format(endDate.getTime()) + "&email=sghitun@yahoo.com")
                .with(bearerToken))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    @Test
    public void shouldReturnValidParametersWhenCalled() throws Exception {

        Integer result = 200;

        Calendar startDate = Calendar.getInstance();
        startDate.add(startDate.MINUTE, 10);

        Calendar endDate = Calendar.getInstance();
        endDate.set(Calendar.MINUTE, startDate.get(Calendar.MINUTE));
        endDate.add(endDate.MINUTE, 30);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/prevalidation?roomId=2&startDate=" + df.format(startDate.getTime()) + "&endDate=" + df.format(endDate.getTime()) + "&email=sghitun@yahoo.com")
                .with(bearerToken))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    public void shouldReturnInValidParametersWhenCalledWithAnotherReservation() throws Exception {

        Integer result = 400;

        Calendar startDate = Calendar.getInstance();
        startDate.add(startDate.MINUTE, 10);

        Calendar endDate = Calendar.getInstance();
        endDate.add(endDate.MINUTE, 40);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/prevalidation?roomId=2&startDate=" + df.format(startDate.getTime()) + "&endDate=" + df.format(endDate.getTime()) + "&email=testEmail@yahoo.com")
                .with(bearerToken))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void shouldReturnValidParametersWhenCalledWithAnotherReservationThatYouOwn() throws Exception {

        Integer result = 200;

        Calendar startDate = Calendar.getInstance();
        startDate.add(startDate.MINUTE, 10);

        Calendar endDate = Calendar.getInstance();
        endDate.set(Calendar.MINUTE, startDate.get(Calendar.MINUTE));
        endDate.add(endDate.MINUTE, 40);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/prevalidation?roomId=2&startDate=" + df.format(startDate.getTime()) + "&endDate=" + df.format(endDate.getTime()) + "&email=sghitun@yahoo.com")
                .with(bearerToken))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void shouldReturnValidParametersWhenCalledWithInnerReservations() throws Exception {

        Integer result = 200;

        Calendar startDate = Calendar.getInstance();
        startDate.add(startDate.MINUTE, 31);
        startDate.set(Calendar.SECOND, 0);
        startDate.set(Calendar.MILLISECOND, 0);

        Calendar endDate = Calendar.getInstance();
        endDate.set(Calendar.HOUR, endDate.get(Calendar.HOUR) + 2);
        endDate.set(Calendar.SECOND, 0);
        endDate.set(Calendar.MILLISECOND, 0);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/prevalidation?roomId=2&startDate=" + df.format(startDate.getTime()) + "&endDate=" + df.format(endDate.getTime()) + "&email=testEmail@yahoo.com")
                .with(bearerToken))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }


}