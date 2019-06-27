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
import java.util.*;

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

   @Test
    public void shouldReturnRoomListIfReservationIsBetweenFilterDatesWhenViewByFieldsIsCalled(){

        UserEntity userEntityPerson = BuildersWrapper.buildPersonEntity(1L,"sghitun@yahoo.com","sghitun", null, UserType.PERSON,"Stefania","Ghitun");
        sut.save(userEntityPerson);
       UserEntity userEntityRoom = BuildersWrapper.buildRoomEntity(2L, "wonderland@yahoo.com", "wonderland",
               new HashSet<>(), BuildersWrapper.buildFloorEntity(1L, 5), UserType.ROOM, "Wonderland", 14);
        sut.save(userEntityRoom);

        List<UserEntity> expectedReservationRoomsList = Collections.singletonList(userEntityRoom);

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

       UserEntity userEntityPerson = BuildersWrapper.buildPersonEntity(1L,"sghitun@yahoo.com","sghitun", null, UserType.PERSON,"Stefania","Ghitun");
       sut.save(userEntityPerson);
       UserEntity userEntityRoom = BuildersWrapper.buildRoomEntity(2L, "wonderland@yahoo.com", "wonderland",
               new HashSet<>(), BuildersWrapper.buildFloorEntity(1L, 5), UserType.ROOM, "Wonderland", 14);
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

        UserEntity userEntityPerson = BuildersWrapper.buildPersonEntity(1L,"sghitun@yahoo.com","sghitun", null, UserType.PERSON,"Stefania","Ghitun");
        sut.save(userEntityPerson);
        UserEntity userEntityRoom = BuildersWrapper.buildRoomEntity(2L, "wonderland@yahoo.com", "wonderland",
                new HashSet<>(), BuildersWrapper.buildFloorEntity(1L, 5), UserType.ROOM, "Wonderland", 14);
        sut.save(userEntityRoom);

        List<UserEntity> expectedReservationRoomsList = Collections.singletonList(userEntityRoom);

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

       UserEntity userEntityPerson = BuildersWrapper.buildPersonEntity(1L,"sghitun@yahoo.com","sghitun", null, UserType.PERSON,"Stefania","Ghitun");
       sut.save(userEntityPerson);
       UserEntity userEntityRoom = BuildersWrapper.buildRoomEntity(2L, "wonderland@yahoo.com", "wonderland",
               new HashSet<>(), BuildersWrapper.buildFloorEntity(1L, 5), UserType.ROOM, "Wonderland", 14);
       sut.save(userEntityRoom);

        List<UserEntity> expectedReservationRoomsList = Collections.singletonList(userEntityRoom);

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

        UserEntity userEntityPerson = BuildersWrapper.buildPersonEntity(1L,"sghitun@yahoo.com","sghitun", null, UserType.PERSON,"Stefania","Ghitun");
        sut.save(userEntityPerson);
        UserEntity userEntityRoom = BuildersWrapper.buildRoomEntity(2L, "wonderland@yahoo.com", "wonderland",
                new HashSet<>(), BuildersWrapper.buildFloorEntity(1L, 5), UserType.ROOM, "Wonderland", 14);
        sut.save(userEntityRoom);

        List<UserEntity> expectedReservationRoomsList = Collections.singletonList(userEntityRoom);

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
    }

    /*
      @Query(value=" select * from users u where u.type='ROOM' and (u.id not in " +
            " (select r.room_id from reservations r where ( " +
            " (r.start_date >= :start_date and r.end_date <= :end_date) or " +
            " (:start_date > r.start_date and :start_date < r.end_date) or" +
            " (:end_date  >  r.start_date and :end_date   < r.end_date)))) and " +
            " ( (select floor from floors f where f.id = u.floor_id) = (CAST (CAST(:floor AS character varying) AS integer)) or :floor is null) and " +
            " (u.max_persons >= (CAST (CAST(:min_persons AS character varying) AS integer)) or :min_persons is null)", nativeQuery=true)
    List<UserEntity> filterByFields(@Param("start_date") Calendar startDate, @Param("end_date") Calendar endDate,
                                    @Param("min_persons") Integer minPersons, @Param("floor") Integer floor);
     */

    @Test
    public void shouldNotReturnRoomWithReservationBeginningBetweenFilterDatesWhenFilterByFieldsIsCalled(){
        UserEntity userEntityPerson = BuildersWrapper.buildPersonEntity(1L,"sghitun@yahoo.com","sghitun", null, UserType.PERSON,"Stefania","Ghitun");
        sut.save(userEntityPerson);
        UserEntity userEntityRoom = BuildersWrapper.buildRoomEntity(2L, "wonderland@yahoo.com", "wonderland",
                new HashSet<>(), BuildersWrapper.buildFloorEntity(1L, 5), UserType.ROOM, "Wonderland", 14);
        sut.save(userEntityRoom);
        Calendar startDate = Calendar.getInstance();
        startDate.set(2019,Calendar.JUNE,28,10,30,0);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2019,Calendar.JUNE,28,11,30,0);
        ReservationEntity reservationEntity = BuildersWrapper.buildReservationEntity(1L,startDate,endDate,"Retro",userEntityPerson,userEntityRoom);
        reservationRepository.save(reservationEntity);

        Calendar filterStartDate = Calendar.getInstance();
        filterStartDate.set(2019,Calendar.JUNE,28,10,0,0);
        Calendar filterEndDate = Calendar.getInstance();
        filterEndDate.set(2019,Calendar.JUNE,28,11,0,0);

        List<UserEntity> actualReservationRoomsList = sut.filterByFields(filterStartDate,filterEndDate,null,null);

        Assert.assertEquals(Collections.emptyList(), actualReservationRoomsList);
    }

    @Test
    public void shouldNotReturnRoomWithReservationEndingBetweenFilterDatesWhenFilterByFieldsIsCalled(){
        UserEntity userEntityPerson = BuildersWrapper.buildPersonEntity(1L,"sghitun@yahoo.com","sghitun", null, UserType.PERSON,"Stefania","Ghitun");
        sut.save(userEntityPerson);
        UserEntity userEntityRoom = BuildersWrapper.buildRoomEntity(2L, "wonderland@yahoo.com", "wonderland",
                new HashSet<>(), BuildersWrapper.buildFloorEntity(1L, 5), UserType.ROOM, "Wonderland", 14);
        sut.save(userEntityRoom);
        Calendar startDate = Calendar.getInstance();
        startDate.set(2019,Calendar.JUNE,28,10,30,0);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2019,Calendar.JUNE,28,11,30,0);
        ReservationEntity reservationEntity = BuildersWrapper.buildReservationEntity(1L,startDate,endDate,"Retro",userEntityPerson,userEntityRoom);
        reservationRepository.save(reservationEntity);

        Calendar filterStartDate = Calendar.getInstance();
        filterStartDate.set(2019,Calendar.JUNE,28,11,0,0);
        Calendar filterEndDate = Calendar.getInstance();
        filterEndDate.set(2019,Calendar.JUNE,28,11,30,0);

        List<UserEntity> actualReservationRoomsList = sut.filterByFields(filterStartDate,filterEndDate,null,null);

        Assert.assertEquals(Collections.emptyList(), actualReservationRoomsList);
    }

    @Test
    public void shouldNotReturnRoomWithReservationInBetweenFilterDatesWhenFilterByFieldsIsCalled(){
        UserEntity userEntityPerson = BuildersWrapper.buildPersonEntity(1L,"sghitun@yahoo.com","sghitun", null, UserType.PERSON,"Stefania","Ghitun");
        sut.save(userEntityPerson);
        UserEntity userEntityRoom = BuildersWrapper.buildRoomEntity(2L, "wonderland@yahoo.com", "wonderland",
                new HashSet<>(), BuildersWrapper.buildFloorEntity(1L, 5), UserType.ROOM, "Wonderland", 14);
        sut.save(userEntityRoom);
        Calendar startDate = Calendar.getInstance();
        startDate.set(2019,Calendar.JUNE,28,10,0,0);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2019,Calendar.JUNE,28,11,0,0);
        ReservationEntity reservationEntity = BuildersWrapper.buildReservationEntity(1L,startDate,endDate,"Retro",userEntityPerson,userEntityRoom);
        reservationRepository.save(reservationEntity);

        Calendar filterStartDate = Calendar.getInstance();
        filterStartDate.set(2019,Calendar.JUNE,28,9,30,0);
        Calendar filterEndDate = Calendar.getInstance();
        filterEndDate.set(2019,Calendar.JUNE,28,11,30,0);

        List<UserEntity> actualReservationRoomsList = sut.filterByFields(filterStartDate,filterEndDate,null,null);

        Assert.assertEquals(Collections.emptyList(), actualReservationRoomsList);
    }

    @Test
    public void shouldNotReturnRoomWithReservationEncompassFilterDatesWhenFilterByFieldsIsCalled(){
        UserEntity userEntityPerson = BuildersWrapper.buildPersonEntity(1L,"sghitun@yahoo.com","sghitun", null, UserType.PERSON,"Stefania","Ghitun");
        sut.save(userEntityPerson);
        UserEntity userEntityRoom = BuildersWrapper.buildRoomEntity(2L, "wonderland@yahoo.com", "wonderland",
                new HashSet<>(), BuildersWrapper.buildFloorEntity(1L, 5), UserType.ROOM, "Wonderland", 14);
        sut.save(userEntityRoom);
        Calendar startDate = Calendar.getInstance();
        startDate.set(2019,Calendar.JUNE,28,9,30,0);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2019,Calendar.JUNE,28,10,30,0);
        ReservationEntity reservationEntity = BuildersWrapper.buildReservationEntity(1L,startDate,endDate,"Retro",userEntityPerson,userEntityRoom);
        reservationRepository.save(reservationEntity);

        Calendar filterStartDate = Calendar.getInstance();
        filterStartDate.set(2019,Calendar.JUNE,28,9,0,0);
        Calendar filterEndDate = Calendar.getInstance();
        filterEndDate.set(2019,Calendar.JUNE,28,10,0,0);

        List<UserEntity> actualReservationRoomsList = sut.filterByFields(filterStartDate,filterEndDate,null,null);

        Assert.assertEquals(Collections.emptyList(), actualReservationRoomsList);
    }

    @Test
    public void shouldNotReturnRoomWithReservationMatchesFilterDatesWhenFilterByFieldsIsCalled(){
        UserEntity userEntityPerson = BuildersWrapper.buildPersonEntity(1L,"sghitun@yahoo.com","sghitun", null, UserType.PERSON,"Stefania","Ghitun");
        sut.save(userEntityPerson);
        UserEntity userEntityRoom = BuildersWrapper.buildRoomEntity(2L, "wonderland@yahoo.com", "wonderland",
                new HashSet<>(), BuildersWrapper.buildFloorEntity(1L, 5), UserType.ROOM, "Wonderland", 14);
        sut.save(userEntityRoom);
        Calendar startDate = Calendar.getInstance();
        startDate.set(2019,Calendar.JUNE,28,9,30,0);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2019,Calendar.JUNE,28,10,30,0);
        ReservationEntity reservationEntity = BuildersWrapper.buildReservationEntity(1L,startDate,endDate,"Retro",userEntityPerson,userEntityRoom);
        reservationRepository.save(reservationEntity);

        Calendar filterStartDate = Calendar.getInstance();
        filterStartDate.set(2019,Calendar.JUNE,28,9,30,0);
        Calendar filterEndDate = Calendar.getInstance();
        filterEndDate.set(2019,Calendar.JUNE,28,10,30,0);

        List<UserEntity> actualReservationRoomsList = sut.filterByFields(filterStartDate,filterEndDate,null,null);

        Assert.assertEquals(Collections.emptyList(), actualReservationRoomsList);
    }

    @Test
    public void shouldNotReturnAllRoomsWhoseReservationIsNotInTheSameDayAsFilterDatesWhenFilterByFieldsIsCalled(){
        UserEntity userEntityPerson = BuildersWrapper.buildPersonEntity(1L,"sghitun@yahoo.com","sghitun", null, UserType.PERSON,"Stefania","Ghitun");
        sut.save(userEntityPerson);
        UserEntity userEntityRoom = BuildersWrapper.buildRoomEntity(2L, "wonderland@yahoo.com", "wonderland",
                new HashSet<>(), BuildersWrapper.buildFloorEntity(1L, 5), UserType.ROOM, "Wonderland", 14);
        sut.save(userEntityRoom);
        Calendar startDate = Calendar.getInstance();
        startDate.set(2019,Calendar.JUNE,27,9,30,0);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2019,Calendar.JUNE,27,10,30,0);
        ReservationEntity reservationEntity = BuildersWrapper.buildReservationEntity(1L,startDate,endDate,"Retro",userEntityPerson,userEntityRoom);
        reservationRepository.save(reservationEntity);

        UserEntity userEntityRoomSameDay = BuildersWrapper.buildRoomEntity(3L, "westeros@yahoo.com", "westeros",
                new HashSet<>(), BuildersWrapper.buildFloorEntity(2L, 8), UserType.ROOM, "Westeros",20);
        sut.save(userEntityRoom);

        Calendar filterStartDate = Calendar.getInstance();
        filterStartDate.set(2019,Calendar.JUNE,28,9,30,0);
        Calendar filterEndDate = Calendar.getInstance();
        filterEndDate.set(2019,Calendar.JUNE,28,10,30,0);

        List<UserEntity> actualReservationRoomsList = sut.filterByFields(filterStartDate,filterEndDate,null,null);

        Assert.assertEquals(Collections.emptyList(), actualReservationRoomsList);
    }
}
