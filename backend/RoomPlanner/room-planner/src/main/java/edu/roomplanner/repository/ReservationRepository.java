package edu.roomplanner.repository;

import edu.roomplanner.entity.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {
    @Query(value = "SELECT * FROM reservations p WHERE p.room_id = ?3 and ((p.start_date >= ?1 and p.end_date <= ?2 ) or (p.end_date >= ?1 and p.end_date <= ?2)" +
            "or (p.start_date >= ?1 and p.end_date >= ?2) or (p.start_date<=?1 and p.end_date<=?2))", nativeQuery = true)
    List<ReservationEntity> findAvailableDate(Date startDate, Date endDate, Long roomId);
}
