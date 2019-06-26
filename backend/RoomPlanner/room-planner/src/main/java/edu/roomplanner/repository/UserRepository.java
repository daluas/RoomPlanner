package edu.roomplanner.repository;

import edu.roomplanner.entity.UserEntity;
import edu.roomplanner.types.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmail(String email);

    List<UserEntity> findByType(UserType type);

    @Query(value=" select * from users u where u.type='ROOM' and (u.id not in " +
            " (select r.room_id from reservations r where ( " +
            " (r.start_date >= :start_date and r.end_date <= :end_date) or " +
            " (:start_date > r.start_date and :start_date < r.end_date) or" +
            " (:end_date  >  r.start_date and :end_date   < r.end_date)))) and " +
            " (u.floor = (CAST (CAST(:floor AS character varying) AS integer)) or :floor is null) and " +
            " (u.max_persons >= (CAST (CAST(:min_persons AS character varying) AS integer)) or :min_persons is null)", nativeQuery=true)
    List<UserEntity> filterByFields(@Param("start_date") Calendar startDate, @Param("end_date") Calendar endDate,
                                    @Param("min_persons") Integer minPersons, @Param("floor") Integer floor);

    @Query(value="select * from users u INNER JOIN reservations r ON r.room_id=u.id where u.type='ROOM' and " +
            "((:start_date <= r.start_date and :end_date >= r.end_date) or " +
            " (:end_date > r.start_date and :start_date < r.start_date) or " +
            " (:start_date < r.end_date and :end_date > r.end_date))", nativeQuery = true)
    List<UserEntity> viewByFields(@Param("start_date") Calendar startDate, @Param("end_date") Calendar endDate);

}
