package edu.roomplanner.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@Setter
@Getter
@NoArgsConstructor
@DiscriminatorValue("person")
public class PersonEntity extends UserEntity {

    @Column(name = "first_name")
    private String firstName;

    @Column(name ="last_name")
    private String lastName;
}
