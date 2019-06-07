package edu.roomplanner.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@DiscriminatorValue("room")
public class RoomEntity extends UserEntity {

    @Column(name = "room_name")
    private String room_name;

    @Column(name = "floor")
    private Integer floor;

    @Column(name = "max_persons")
    private Integer maxPersons;

    @Override
    public String toString() {
        return "RoomEntity{" +
                "room_name='" + room_name + '\'' +
                ", floor=" + floor +
                ", maxPersons=" + maxPersons +
                '}';
    }
}
