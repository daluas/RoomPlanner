package edu.roomplanner.entity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@Getter
@Setter
@DiscriminatorValue("ROOM")
public class RoomEntity extends UserEntity {

    @Column(name = "room_name")
    private String name;

    @Column(name = "floor")
    private Integer floor;

    @Column(name = "max_persons")
    private Integer maxPersons;
}
