package edu.roomplanner.builders;

import edu.roomplanner.entity.FloorEntity;
import edu.roomplanner.entity.RoomEntity;

import java.util.Set;

public class FloorEntityBuilder {

    private Long id;
    private Integer floor;
    private Set<RoomEntity> rooms;

    public static FloorEntityBuilder builder() {
        return new FloorEntityBuilder();
    }

    public FloorEntityBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public FloorEntityBuilder withFloor(Integer floor) {
        this.floor = floor;
        return this;
    }

    public FloorEntityBuilder withRooms(Set<RoomEntity> rooms) {
        this.rooms = rooms;
        return this;
    }

    public FloorEntity build() {

        FloorEntity floorEntity = new FloorEntity();
        floorEntity.setId(id);
        floorEntity.setFloor(floor);
        floorEntity.setRooms(rooms);

        return floorEntity;
    }

}
