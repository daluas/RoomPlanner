package edu.roomplanner.entity;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.Set;


@Entity
@Setter
@Getter
@DiscriminatorValue("PERSON")
public class PersonEntity extends UserEntity {

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @OneToMany(mappedBy = "person", cascade= javax.persistence.CascadeType.ALL)
    private Set<ReservationEntity> reservations;
}
