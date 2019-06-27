package edu.roomplanner.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.Set;

import javax.persistence.*;

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

    @OneToMany(mappedBy = "room", cascade= javax.persistence.CascadeType.ALL)
    private Set<ReservationEntity> reservations;

}
