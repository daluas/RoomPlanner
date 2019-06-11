package edu.roomplanner.repository;

import edu.roomplanner.entity.UserEntity;
import edu.roomplanner.types.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    List<UserEntity> findByType(UserType type);
}
