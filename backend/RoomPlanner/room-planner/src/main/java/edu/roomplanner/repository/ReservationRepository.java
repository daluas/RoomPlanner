package edu.roomplanner.repository;

import edu.roomplanner.entity.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Calendar;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {

    @Query(value = "SELECT * FROM reservations p WHERE p.room_id = :room_id and ((p.start_date >= :start_date and p.end_date <= :end_date ) or (p.end_date >= start_date and p.end_date <= :end_date)" +
            "or (p.start_date >= :start_date and p.end_date >= :end_date) or (p.start_date <= :start_date and p.end_date <= :end_date))", nativeQuery = true)
    List<ReservationEntity> findAvailableDate(@Param("start_date") Calendar startDate, @Param("end_date") Calendar endDate, @Param("room_id") Long roomId);

}