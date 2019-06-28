package edu.roomplanner.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
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

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL)
    private Set<ReservationEntity> reservations;

}
