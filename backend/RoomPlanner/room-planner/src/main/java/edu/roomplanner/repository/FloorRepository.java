package edu.roomplanner.repository;

import edu.roomplanner.entity.FloorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FloorRepository extends JpaRepository<FloorEntity,Long> {
}
