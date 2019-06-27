package edu.roomplanner.repository;

import edu.roomplanner.entity.FloorEntity;
import edu.roomplanner.entity.ReservationEntity;
import edu.roomplanner.entity.UserEntity;
import edu.roomplanner.types.UserType;
import edu.roomplanner.util.BuildersWrapper;
import org.flywaydb.core.Flyway;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

@RunWith(SpringRunner.class)
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private Flyway flyway;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private
    UserRepository sut;

    @Autowired
    private FloorRepository floorRepository;

    @Autowired
    private
    ReservationRepository reservationRepository;


    @Before
    public void init() {
        flyway.clean();
        flyway.migrate();

        reservationRepository.deleteAll();
        sut.deleteAll();

       entityManager.createNativeQuery("ALTER SEQUENCE seq_user_id RESTART WITH 1").executeUpdate();
       entityManager.createNativeQuery("ALTER SEQUENCE seq_reservation_id RESTART WITH 1").executeUpdate();

    }

   /* @Test
    public void shouldReturnRoomListIfReservationIsBetweenFilterDatesWhenViewByFieldsIsCalled(){

        FloorEntity roomEntityFloor = BuildersWrapper.buildFloorEntity(1L,5);
        floorRepository.save(roomEntityFloor);

        UserEntity userEntityPerson = BuildersWrapper.buildPersonEntiy(1L,"sghitun@yahoo.com","sghitun",
                UserType.PERSON,"Stefania","Ghitun");
        sut.save(userEntityPerson);
        UserEntity userEntityRoom = BuildersWrapper.buildRoomEntity(2L, "wonderland@yahoo.com", "wonderland",
                UserType.ROOM, "Wonderland", roomEntityFloor, 14);
        sut.save(userEntityRoom);

        List<UserEntity> expectedReservationRoomsList = Arrays.asList(userEntityRoom);

        Calendar startDate = Calendar.getInstance();
        startDate.set(2019,Calendar.JUNE,22,10,0,0);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2019,Calendar.JUNE,22,11,30,0);

        ReservationEntity reservationEntity = BuildersWrapper.buildReservationEntity(1L,startDate,endDate,"Demo meeting",userEntityPerson,userEntityRoom);
        reservationRepository.save(reservationEntity);

        Calendar filterStartDate = Calendar.getInstance();
        filterStartDate.set(2019,Calendar.JUNE,22,9,30,0);
        Calendar filterEndDate = Calendar.getInstance();
        filterEndDate.set(2019,Calendar.JUNE,22,12,0,0);

        List<UserEntity> actualReservationRoomsList = sut.viewByFields(filterStartDate,filterEndDate);

        Assert.assertEquals(expectedReservationRoomsList, actualReservationRoomsList);
    }

    @Test
    public void shouldReturnEmptyRoomListIfReservationIsOutOfFilterDatesBoundsWhenViewByFieldsIsCalled(){



        UserEntity userEntityPerson = BuildersWrapper.buildPersonEntiy(1L,"sghitun@yahoo.com","sghitun",
                UserType.PERSON,"Stefania","Ghitun");
        sut.save(userEntityPerson);
        UserEntity userEntityRoom = BuildersWrapper.buildRoomEntity(2L, "wonderland@yahoo.com", "wonderland",
                UserType.ROOM, "Wonderland", 5, 14);
        sut.save(userEntityRoom);

        Calendar startDate = Calendar.getInstance();
        startDate.set(2019,Calendar.JUNE,22,10,0,0);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2019,Calendar.JUNE,22,11,30,0);

        ReservationEntity reservationEntity = BuildersWrapper.buildReservationEntity(1L,startDate,endDate,"Demo meeting",userEntityPerson,userEntityRoom);
        reservationRepository.save(reservationEntity);

        Calendar filterStartDate = Calendar.getInstance();
        filterStartDate.set(2019,Calendar.JUNE,22,8,30,0);
        Calendar filterEndDate = Calendar.getInstance();
        filterEndDate.set(2019,Calendar.JUNE,22,9,0,0);

        List<UserEntity> actualReservationRoomsList = sut.viewByFields(filterStartDate,filterEndDate);

        Assert.assertEquals(Collections.emptyList(), actualReservationRoomsList);
    }

    @Test
    public void shouldReturnRoomListIfReservationStartDateIsBeforeStartFilterAndEndDateIsBeforeEndFilterWhenViewByFieldsIsCalled(){

        UserEntity userEntityPerson = BuildersWrapper.buildPersonEntiy(1L,"sghitun@yahoo.com","sghitun",
                UserType.PERSON,"Stefania","Ghitun");
        sut.save(userEntityPerson);
        UserEntity userEntityRoom = BuildersWrapper.buildRoomEntity(2L, "wonderland@yahoo.com", "wonderland",
                UserType.ROOM, "Wonderland", 5, 14);
        sut.save(userEntityRoom);

        List<UserEntity> expectedReservationRoomsList = Arrays.asList(userEntityRoom);

        Calendar startDate = Calendar.getInstance();
        startDate.set(2019,Calendar.JUNE,22,10,0,0);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2019,Calendar.JUNE,22,11,30,0);

        ReservationEntity reservationEntity = BuildersWrapper.buildReservationEntity(1L,startDate,endDate,"Demo meeting",userEntityPerson,userEntityRoom);
        reservationRepository.save(reservationEntity);

        Calendar filterStartDate = Calendar.getInstance();
        filterStartDate.set(2019,Calendar.JUNE,22,11,0,0);
        Calendar filterEndDate = Calendar.getInstance();
        filterEndDate.set(2019,Calendar.JUNE,22,12,0,0);

        List<UserEntity> actualReservationRoomsList = sut.viewByFields(filterStartDate,filterEndDate);

        Assert.assertEquals(expectedReservationRoomsList, actualReservationRoomsList);
    }

    @Test
    public void shouldReturnRoomListIfReservationStartDateIsSameAsStartFilterAndEndDateIsSameAsEndFilterWhenViewByFieldsIsCalled(){

        UserEntity userEntityPerson = BuildersWrapper.buildPersonEntiy(1L,"sghitun@yahoo.com","sghitun",
                UserType.PERSON,"Stefania","Ghitun");
        sut.save(userEntityPerson);
        UserEntity userEntityRoom = BuildersWrapper.buildRoomEntity(2L, "wonderland@yahoo.com", "wonderland",
                UserType.ROOM, "Wonderland", 5, 14);
        sut.save(userEntityRoom);

        List<UserEntity> expectedReservationRoomsList = Arrays.asList(userEntityRoom);

        Calendar startDate = Calendar.getInstance();
        startDate.set(2019,Calendar.JUNE,22,10,0,0);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2019,Calendar.JUNE,22,11,30,0);

        ReservationEntity reservationEntity = BuildersWrapper.buildReservationEntity(1L,startDate,endDate,"Demo meeting",userEntityPerson,userEntityRoom);
        reservationRepository.save(reservationEntity);

        Calendar filterStartDate = Calendar.getInstance();
        filterStartDate.set(2019,Calendar.JUNE,22,10,0,0);
        Calendar filterEndDate = Calendar.getInstance();
        filterEndDate.set(2019,Calendar.JUNE,22,11,30,0);

        List<UserEntity> actualReservationRoomsList = sut.viewByFields(filterStartDate,filterEndDate);

        Assert.assertEquals(expectedReservationRoomsList, actualReservationRoomsList);
    }

    @Test
    public void shouldReturnRoomListIfReservationStartDateIsAfterStartFilterAndEndDateIsAfterEndFilterWhenViewByFieldsIsCalled(){

        UserEntity userEntityPerson = BuildersWrapper.buildPersonEntiy(1L,"sghitun@yahoo.com","sghitun",
                UserType.PERSON,"Stefania","Ghitun");
        sut.save(userEntityPerson);
        UserEntity userEntityRoom = BuildersWrapper.buildRoomEntity(2L, "wonderland@yahoo.com", "wonderland",
                UserType.ROOM, "Wonderland", 5, 14);
        sut.save(userEntityRoom);

        List<UserEntity> expectedReservationRoomsList = Arrays.asList(userEntityRoom);

        Calendar startDate = Calendar.getInstance();
        startDate.set(2019,Calendar.JUNE,22,10,0,0);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2019,Calendar.JUNE,22,11,30,0);

        ReservationEntity reservationEntity = BuildersWrapper.buildReservationEntity(1L,startDate,endDate,"Demo meeting",userEntityPerson,userEntityRoom);
        reservationRepository.save(reservationEntity);

        Calendar filterStartDate = Calendar.getInstance();
        filterStartDate.set(2019,Calendar.JUNE,22,9,0,0);
        Calendar filterEndDate = Calendar.getInstance();
        filterEndDate.set(2019,Calendar.JUNE,22,11,0,0);

        List<UserEntity> actualReservationRoomsList = sut.viewByFields(filterStartDate,filterEndDate);

        Assert.assertEquals(expectedReservationRoomsList, actualReservationRoomsList);
    }*/
}
