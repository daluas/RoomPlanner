package edu.roomplanner.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@DiscriminatorValue("ROOM")
public class RoomEntity extends UserEntity {

    @Column(name = "room_name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "floor_id", nullable = false)
    private FloorEntity floor;

    @Column(name = "max_persons")
    private Integer maxPersons;

    @OneToMany(mappedBy = "room")
    private Set<ReservationEntity> reservations;

}
