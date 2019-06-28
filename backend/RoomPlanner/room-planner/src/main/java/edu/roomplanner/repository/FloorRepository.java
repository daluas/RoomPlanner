package edu.roomplanner.repository;

import edu.roomplanner.entity.FloorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FloorRepository extends JpaRepository<FloorEntity, Long> {

    Optional<FloorEntity> findByFloor(Integer floor);
}
