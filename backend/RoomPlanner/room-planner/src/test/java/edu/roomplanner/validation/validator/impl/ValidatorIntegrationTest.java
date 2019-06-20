package edu.roomplanner.validation.validator.impl;

import edu.roomplanner.RoomPlannerApplication;
import edu.roomplanner.builders.ReservationEntityBuilder;
import edu.roomplanner.entity.PersonEntity;
import edu.roomplanner.entity.ReservationEntity;
import edu.roomplanner.entity.RoomEntity;
import edu.roomplanner.repository.ReservationRepository;
import edu.roomplanner.repository.UserRepository;
import edu.roomplanner.types.UserType;
import org.flywaydb.core.Flyway;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = RoomPlannerApplication.class
)
@TestPropertySource(locations = "classpath:application-test.properties")
public class ValidatorIntegrationTest {

    @Autowired
    private Flyway flyway;

    @Before
    public void init() throws Exception {
        flyway.clean();
        flyway.migrate();
    }


    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void shouldNotFindAValidEqualsDates() {
        ReservationEntity reservation = getReservationEntity(2007, 6, 6, 6,
                2007, 7, 8, 5);
        reservationRepository.save(reservation);

        List<ReservationEntity> actualList = reservationRepository.findAvailableDate(reservation.getStartDate(), reservation.getEndDate(), reservation.getRoom().getId());
        List<ReservationEntity> expectedList = Arrays.asList(reservation);

        Assert.assertEquals(expectedList.isEmpty(), actualList.isEmpty());

    }

    @Test
    public void shouldFindAValidDate() {
        ReservationEntity reservation = getReservationEntity(2007, 6, 11, 45,
                2007, 6, 11, 20);
        ReservationEntity existReservation = getReservationEntity(2007, 6, 11, 20,
                2007, 6, 12, 10);
        reservationRepository.save(existReservation);

        List<ReservationEntity> actualList = reservationRepository.findAvailableDate(reservation.getStartDate(), reservation.getEndDate(), reservation.getRoom().getId());

        Assert.assertEquals(new ArrayList<>().isEmpty(), actualList.isEmpty());

    }

    private ReservationEntity getReservationEntity(int startYear, int startDay, int startHour, int startMinute,
                                                   int endYear, int endDay, int endHour, int endMinute) {

        PersonEntity person = getPerson(1, "person1");
        RoomEntity room = getRoom(2, "room1");

        userRepository.save(person);
        userRepository.save(room);

        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        startDate.set(startYear, Calendar.JANUARY, startDay, startHour, startMinute, 0);
        endDate.set(endYear, Calendar.JANUARY, endDay, endHour, endMinute, 0);

        return new ReservationEntityBuilder()
                .withStartDate(startDate)
                .withEndDate(endDate)
                .withPerson(person)
                .withRoom(room)
                .build();

    }

    private PersonEntity getPerson(long id, String email) {

        PersonEntity person = new PersonEntity();
        person.setId(id);
        person.setEmail(email);
        person.setPassword("personPass");
        person.setType(UserType.PERSON);

        return person;
    }

    private RoomEntity getRoom(long id, String email) {

        RoomEntity room = new RoomEntity();
        room.setId(id);
        room.setEmail(email);
        room.setPassword("roomPass");
        room.setType(UserType.ROOM);

        return room;
    }

}
