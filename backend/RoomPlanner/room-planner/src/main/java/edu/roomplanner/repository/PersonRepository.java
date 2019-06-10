package edu.roomplanner.repository;

import edu.roomplanner.entity.PersonEntity;
import edu.roomplanner.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collections;
import java.util.Optional;

public interface PersonRepository extends JpaRepository<PersonEntity, Long> {

    default Optional<PersonEntity> findByEmail(String email) {
        if(email.equals("user1@cegeka.com")) {
            PersonEntity personEntity = new PersonEntity();
            personEntity.setId(1);
            personEntity.setEmail(email);
            personEntity.setType("User");
            personEntity.setPassword("user1");
            personEntity.setFirstName("fUser");
            personEntity.setLastName("lUser");
            RoleEntity roleEntity = new RoleEntity();
            roleEntity.setId(1);
            roleEntity.setName("user");
            personEntity.setRoleEntityList(Collections.singletonList(roleEntity));
            return Optional.of(personEntity);
        }
        return Optional.empty();
    }

}
