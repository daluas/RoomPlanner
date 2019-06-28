package edu.roomplanner.repository;

import edu.roomplanner.entity.FloorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FloorRepository extends JpaRepository<FloorEntity, Long> {

    FloorEntity findByFloor(Integer floor);
}
