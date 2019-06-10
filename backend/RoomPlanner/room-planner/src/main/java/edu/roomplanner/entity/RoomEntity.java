package edu.roomplanner.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@DiscriminatorValue("room")
public class RoomEntity extends UserEntity {

    @Column(name = "room_name")
    private String roomName;

    @Column(name = "floor")
    private Integer floor;

    @Column(name = "max_persons")
    private Integer maxPersons;
}
